package com.application.orderService.SpringBootRest.order.config.listener;

import com.application.orderService.SpringBootRest.order.config.RabbitMQConfig;
import com.application.orderService.SpringBootRest.order.config.messaging.StockUpdateConfirmation;
import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderConfirmationListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmationListener.class);
    private final OrderRepository orderRepository;

    public OrderConfirmationListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_CONFIRMATION_QUEUE)
    public void handleStockUpdateConfirmation(StockUpdateConfirmation confirmation) {
        logger.info("Recibiendo confirmacion: {}", confirmation);
        Optional<Order> optionalOrder = orderRepository.findById(confirmation.getOrderId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            //Se actualiza solo si la orden sigue en "processing"
            if (order.getStatus() == OrderStatus.PROCESSING) {
                if (confirmation.isSuccess()) {
                    order.setStatus(OrderStatus.COMPLETED);
                } else {
                    order.setStatus(OrderStatus.FAILED);
                }
                orderRepository.save(order);
                logger.info("Order {} update to {}", order.getId(), order.getStatus());
            }
        }

    }

}
