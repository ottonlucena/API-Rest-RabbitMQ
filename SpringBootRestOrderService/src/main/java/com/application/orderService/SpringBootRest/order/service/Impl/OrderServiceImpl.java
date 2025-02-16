package com.application.orderService.SpringBootRest.order.service.Impl;

import com.application.orderService.SpringBootRest.order.config.RabbitMQConfig;
import com.application.orderService.SpringBootRest.order.config.messaging.ProductoDTO;
import com.application.orderService.SpringBootRest.order.config.messaging.StockUpdateMessage;
import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.repository.OrderRepository;
import com.application.orderService.SpringBootRest.order.repository.UserRepository;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;


    @Value("${dataservice.url}")
    private String dataServiceUrl;

    public OrderServiceImpl(OrderRepository orderRepository, RestTemplate restTemplate, UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Order createOrder(Order order) {

        //Verificamos que el User exista mediante su ID
        userRepository.findById(order.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String productEndpoint = dataServiceUrl + "/api/products/" + order.getProductId();
        try {
            ProductoDTO product = restTemplate.getForObject(productEndpoint, ProductoDTO.class);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify product: " + e.getMessage());
        }
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findByIdOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order processOrder(Long orderId) {

        //Obtengo la orden
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //Verificamos que la orden no este en estado pendiente
        if (order.getStatus() != OrderStatus.PENDING){
            throw new IllegalStateException("Order already processed");
        }

        //Consulto el producto
        String productEndpoint = dataServiceUrl + "/api/products/" + order.getProductId();
        ProductoDTO productDTO = restTemplate.getForObject(productEndpoint, ProductoDTO.class);
        if (productDTO == null) {
            throw new RuntimeException("Product not found");
        }
        //Validamos su stock
        if (order.getQuantity() > productDTO.getStock()) {
            throw new RuntimeException("Stock insuficiente");
        }

        //Enviamos el mensaje de actualización de manera Asíncrona a RabbitMQ para actualizar el stock
        StockUpdateMessage message = StockUpdateMessage.builder()
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_QUEUE, message);

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }
}
