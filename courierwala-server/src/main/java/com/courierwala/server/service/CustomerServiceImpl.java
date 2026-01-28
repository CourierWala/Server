package com.courierwala.server.service;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courierwala.server.customerdto.CustomerProfileDto;
import com.courierwala.server.customerdto.CustomerProfileUpdateDto;
import com.courierwala.server.customerdto.ShipmentRequest;
import com.courierwala.server.customerdto.ShipmentResDto;
import com.courierwala.server.dto.OrderStatusEvent;
import com.courierwala.server.dto.RoutingResult;
import com.courierwala.server.entities.Address;
import com.courierwala.server.entities.City;
import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.User;
import com.courierwala.server.enumfield.DeliveryType;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.enumfield.PackageSize;
import com.courierwala.server.enumfield.PaymentStatus;
import com.courierwala.server.events.OrderEventPublisher;
import com.courierwala.server.repository.AddressRepository;
import com.courierwala.server.repository.CityRepository;
import com.courierwala.server.repository.CourierOrderRepository;
import com.courierwala.server.repository.UserRepository;
import com.courierwala.server.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {


	private final CityRepository cityRepository;
	private final AddressRepository addressRepository;
	private final CourierOrderRepository courierOrderRepository;
	private final UserRepository userRepository;
    private final ShipmentRoutingService shipmentRoutingService;
    private final OrderHubPathService orderHubPathService;
    private final OrderEventPublisher orderEventPublisher;
	


	@Override
	public ShipmentResDto createShipment(ShipmentRequest req) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
				                 
        System.out.println("role : " + user.getAuthorities());
		User customer = getLoggedInUser(user.getId());

		City pickupCity = getOrCreateCity(req.getPickupCity());
		City deliveryCity = getOrCreateCity(req.getDeliveryCity());

		Address pickupAddress = createAddress(customer, req.getPickupAddress(), req.getPickupPincode(), pickupCity, req.getPickupLatitude(),
				         req.getPickupLongitude());

		Address deliveryAddress = createAddress(customer, req.getDeliveryAddress(), req.getDeliveryPincode(),
				deliveryCity, req.getDeliveryLatitude(), req.getDeliveryLongitude());												

		System.out.println("before calling ===========================================================================");
		RoutingResult routing = shipmentRoutingService.decideRouting(req.getPickupLatitude(), req.getPickupLongitude(),
				req.getDeliveryLatitude(), req.getDeliveryLongitude());
		
		System.out.println("after calling ======================================================================");

		CourierOrder order = CourierOrder.builder().trackingNumber(generateTrackingNumber()).customer(customer)
				.pickupAddress(pickupAddress).deliveryAddress(deliveryAddress).sourceHub(routing.getSourceHub())
				.destinationHub(routing.getDestinationHub()).currentHub(routing.getSourceHub())
				.distanceKm(routing.getDistanceKm()).packageWeight(req.getWeight())
				.packageSize(PackageSize.valueOf(req.getPackageSize()))
				.deliveryType(DeliveryType.valueOf(req.getDeliveryType())).pickupDate(req.getPickupDate())
				.orderStatus(OrderStatus.AT_SOURCE_HUB)
				.paymentStatus(PaymentStatus.PENDING).paymentRequired(true).packageDescription(req.getDescription())
				.build();

		courierOrderRepository.save(order);

		if (!routing.isDirect()) {
			orderHubPathService.savePath(order, routing.getHubPath());
		}
		
		  //  Publish CREATED event
	    OrderStatusEvent event = new OrderStatusEvent(
	        order.getId(),
	        OrderStatus.CREATED.name(),
	        customer.getId(),
	        "Order created",
	        LocalDateTime.now()
	    );

	    orderEventPublisher.publishOrderStatusEvent(event);
		
		 return new ShipmentResDto(order.getId(),"Shipment created successfully ","success");

	}		

	/* ---------------- helper methods ---------------- */

	private City getOrCreateCity(String cityName) {
		return cityRepository.findByCityNameIgnoreCase(cityName).orElseGet(() -> cityRepository.save(City.builder()
				.cityName(cityName).build()));
	}

	private Address createAddress(User user, String street, String pincode, City city, double lat, double lng) {

		Address address = Address.builder().user(user).streetAddress(street).pincode(pincode).city(city)
		         .latitude(lat)
		         .longitude(lng)
				.isDefault(false).build();

		return addressRepository.save(address);
	}

	private String generateTrackingNumber() {
		return "CW" + System.currentTimeMillis();
	}

	private User getLoggedInUser(Long id) {
		// later: SecurityContextHolder
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

       
   
  
  

	
	
	
  
   // ================= VIEW PROFILE =================
    @Override
    public CustomerProfileDto getCustomerProfile(Long customerId) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        return CustomerProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
  
  
   // ================= UPDATE PROFILE =================
    @Override
    public void updateCustomerProfile(Long customerId, CustomerProfileUpdateDto dto) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        userRepository.save(user);
    }

}
