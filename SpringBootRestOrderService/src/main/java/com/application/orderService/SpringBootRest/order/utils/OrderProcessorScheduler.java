package com.application.orderService.SpringBootRest.order.utils;

import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.repository.OrderRepository;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderProcessorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessorScheduler.class);
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderProcessorScheduler(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    //Ejecución cada 5 minutos
    @Scheduled(fixedDelay = 120000)
    public void processPendingOrders(){
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pendingOrders){
            try{
                //Verficamos si la orden sigue pendiente
                if (order.getStatus() != OrderStatus.PENDING){
                    logger.info("Order {} ya procesada, se omite.", order.getId());
                    continue;
                }
                //Procesa la orden, validamos stock y enviamos mensaje
                orderService.processOrder(order.getId());
                logger.info("Procesada la orden: {}", order.getId());
            }catch (Exception e){
                logger.error("Error al procesa la orde {}: {}", order.getId(), e.getMessage());
            }
        }



    }

}
