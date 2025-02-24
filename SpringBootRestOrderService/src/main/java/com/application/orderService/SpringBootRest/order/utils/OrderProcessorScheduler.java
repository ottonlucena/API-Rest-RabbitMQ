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

    //Ejecución cada 3 minutos
    @Scheduled(fixedDelay = 120000)
    public void processPendingOrders() {
        //Buscamos ordenes pednientes
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

        //VErificamos si pending esta vacio y enviarmos un log de advertencia
        if (pendingOrders.isEmpty()) {
            logger.info("Orders pending not found");
            return;
        }
        //Enviamos un log con las ordenes pedientes antes de procesarlas
        logger.info("Found {} pending orders", pendingOrders.size());

        pendingOrders.forEach(order -> {
            try {
                //Procesamos la orden de forma sincronica
                orderService.processOrder(order.getId());
                logger.info("Order {} processed successfully", order.getId());
            } catch (Exception ex) {
                //Registramos el error
                logger.error("Error processing order {}: {}", order.getId(), ex.getMessage());
            }
        });

    }

}
