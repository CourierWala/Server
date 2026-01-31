package com.courierwala.server.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.courierwala.server.customerdto.*;
import com.courierwala.server.entities.*;
import com.courierwala.server.exception.ResourceNotFoundException;
import com.courierwala.server.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.server.dto.OrderStatusEvent;
import com.courierwala.server.dto.RoutingResult;
import com.courierwala.server.dto.ShipmentCreatedEvent;
import com.courierwala.server.enumfield.DeliveryType;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.PackageSize;
import com.courierwala.server.enumfield.PaymentStatus;
import com.courierwala.server.events.OrderEventPublisher;
import com.courierwala.server.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class CustomerServiceImpl implements CustomerService {

	private final CityRepository cityRepository;
	private final AddressRepository addressRepository;
	private final CourierOrderRepository courierOrderRepository;
	private final UserRepository userRepository;
	private final ShipmentRoutingService shipmentRoutingService;
	private final OrderHubPathService orderHubPathService;
	private final OrderEventPublisher orderEventPublisher;
	private final PricingConfigRepository pricingConfigRepository;

	@Override
	public ShipmentResDto createShipment(ShipmentRequest req) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//
//        System.out.println("role : " + user.getAuthorities());
		User customer = getLoggedInUser(user.getId());

		City pickupCity = getOrCreateCity(req.getPickupCity());
		City deliveryCity = getOrCreateCity(req.getDeliveryCity());

		Address pickupAddress = createAddress(customer, req.getPickupAddress(), req.getPickupPincode(), pickupCity,
				req.getPickupLatitude(), req.getPickupLongitude());

		Address deliveryAddress = createAddress(customer, req.getDeliveryAddress(), req.getDeliveryPincode(),
				deliveryCity, req.getDeliveryLatitude(), req.getDeliveryLongitude());

		System.out
				.println("before calling ===========================================================================");
		RoutingResult routing = shipmentRoutingService.decideRouting(req.getPickupLatitude(), req.getPickupLongitude(),
				req.getDeliveryLatitude(), req.getDeliveryLongitude());

		System.out.println("after calling ======================================================================");
		
		
		

		PricingConfig pricing = pricingConfigRepository.findAll().stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Pricing config not found"));

		Double pri = pricing.getBasePrice() + (req.getWeight() * pricing.getPricePerKg())
				+ (pricing.getPricePerKm() * routing.getDistanceKm());
		System.out.println(pri);
		CourierOrder order = CourierOrder.builder().price(pri).trackingNumber(generateTrackingNumber())
				.customer(customer).pickupAddress(pickupAddress).deliveryAddress(deliveryAddress)
				.sourceHub(routing.getSourceHub()).destinationHub(routing.getDestinationHub())
				.currentHub(routing.getSourceHub()).distanceKm(routing.getDistanceKm()).packageWeight(req.getWeight())
				.packageSize(PackageSize.valueOf(req.getPackageSize().toUpperCase()))
				.deliveryType(DeliveryType.valueOf(req.getDeliveryType().toUpperCase())).pickupDate(req.getPickupDate())
				.orderStatus(OrderStatus.CREATED).paymentStatus(PaymentStatus.PENDING).paymentRequired(true)
				.packageDescription(req.getDescription()).build();

		courierOrderRepository.save(order);

		if (!routing.isDirect()) {
			orderHubPathService.savePath(order, routing.getHubPath());
		}

//		LocalDateTime now = LocalDateTime.now();
//
//		OrderStatusEvent event = new OrderStatusEvent(order.getId(), OrderStatus.CREATED.name(), customer.getId(),
//				"Order created", now);
//
//		ShipmentCreatedEvent snapshotEvent = new ShipmentCreatedEvent(order.getId(), order.getTrackingNumber(),
//				pickupCity.getCityName(), deliveryCity.getCityName(), OrderStatus.CREATED.name(), now);
//
//		try {
//			orderEventPublisher.publishShipmentCreatedEvent(snapshotEvent);
//			orderEventPublisher.publishOrderStatusEvent(event);
//		} catch (Exception ex) {
//			log.error("Failed to publish shipment events", ex);
//		}

		return new ShipmentResDto(order.getId(), pri, "Shipment created successfully ", "success");

	}

	/* ---------------- helper methods ---------------- */

	private City getOrCreateCity(String cityName) {
		return cityRepository.findByCityNameIgnoreCase(cityName)
				.orElseGet(() -> cityRepository.save(City.builder().cityName(cityName).build()));
	}

	private Address createAddress(User user, String street, String pincode, City city, double lat, double lng) {

		Address address = Address.builder().user(user).streetAddress(street).pincode(pincode).city(city).latitude(lat)
				.longitude(lng).isDefault(false).build();

		return addressRepository.save(address);
	}

	private String generateTrackingNumber() {
		return "CW" + System.currentTimeMillis();
	}

	private User getLoggedInUser(Long id) {
		// later: SecurityContextHolder
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	private Long getUserId() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		return user.getId();
	}

	// ================= VIEW PROFILE =================
	@Override
	public CustomerProfileDto getCustomerProfile() {

		System.out.println("in customer get profile !!");
		Long customerId = getUserId();
		User user = userRepository.findById(customerId)
				.orElseThrow(() -> new IllegalStateException("Customer not found"));

		return CustomerProfileDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.phone(user.getPhone()).build();
	}

	// ================= UPDATE PROFILE =================
	@Override
	public void updateCustomerProfile(CustomerProfileUpdateDto dto) {
		Long customerId = getUserId();
		User user = userRepository.findById(customerId)
				.orElseThrow(() -> new IllegalStateException("Customer not found"));

		user.setName(dto.getName());
		user.setPhone(dto.getPhone());

		userRepository.save(user);
	}

	@Override
	public List<ShipmentSummaryDto> getAllMyShipments() {

		// TEMP: using fixed customer (same as createShipment)

		User customer = getLoggedInUser(getUserId());

		return courierOrderRepository.findByCustomerOrderByCreatedAtDesc(customer).stream()
				.map(order -> new ShipmentSummaryDto(order.getId(), order.getTrackingNumber(),
						order.getPickupAddress().getCity().getCityName(),
						order.getDeliveryAddress().getCity().getCityName(), order.getOrderStatus(), order.getPrice(),
						order.getPickupDate()))
				.toList();
	}

	public String getTrackingNumberById(Long id){

		CourierOrder courierOrder = courierOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Order Exist With This Id !"));

		return courierOrder.getTrackingNumber();
	}

}
