package com.courierwala.server.scheduler;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.courierwala.server.entities.CourierOrder;
import com.courierwala.server.entities.Hub;
import com.courierwala.server.entities.OrderHubPath;
import com.courierwala.server.enumfield.OrderStatus;
import com.courierwala.server.repository.CourierOrderRepository;
import com.courierwala.server.repository.OrderHubPathRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubMovementScheduler {

    private final CourierOrderRepository courierOrderRepository;
    private final OrderHubPathRepository orderHubPathRepository;

    /**
     * Runs every 2 minutes
     */
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void moveParcelsBetweenHubs() {

        List<CourierOrder> orders =
                courierOrderRepository.findHubRoutedOrders();

        if (orders.isEmpty()) {
            log.info("No hub-routed orders to process");
            return;
        }

        for (CourierOrder order : orders) {

            Optional<OrderHubPath> nextOpt =
                    orderHubPathRepository
                        .findFirstByOrderAndCompletedFalseOrderBySequenceAsc(order);

            //  SAFETY CHECK
            if (nextOpt.isEmpty()) {
                continue;
            }

            OrderHubPath nextPath = nextOpt.get();

            Hub nextHub = nextPath.getHub();

            // Move parcel to next hub
            order.setCurrentHub(nextHub);
            order.setOrderStatus(OrderStatus.IN_TRANSIT);

            // Mark this hub as completed
            nextPath.setCompleted(true);

            orderHubPathRepository.save(nextPath);
            courierOrderRepository.save(order);

            log.info(
                "Order {} moved to hub {}",
                order.getTrackingNumber(),
                nextHub.getHubName()
            );

            //  Check if destination hub reached
            if (nextHub.equals(order.getDestinationHub())) {

                order.setOrderStatus(OrderStatus.AT_DESTINATION_HUB);
                courierOrderRepository.save(order);

                log.info(
                    "Order {} reached destination hub {}",
                    order.getTrackingNumber(),
                    nextHub.getHubName()
                );
            }
        }
    }
}
