package com.application.orderService.SpringBootRest.order.service.Impl;

import com.application.orderService.SpringBootRest.order.config.RabbitMQConfig;
import com.application.orderService.SpringBootRest.order.config.messaging.StockUpdateMessage;
import com.application.orderService.SpringBootRest.order.dto.OrderDTO;
import com.application.orderService.SpringBootRest.order.dto.ProductoDTO;
import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.exception.OrderNotFoundException;
import com.application.orderService.SpringBootRest.order.exception.ProductNotFoundException;
import com.application.orderService.SpringBootRest.order.exception.UserNotFoundException;
import com.application.orderService.SpringBootRest.order.repository.OrderRepository;
import com.application.orderService.SpringBootRest.order.repository.UserRepository;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;

    @Value("${dataservice.url}")
    private String dataServiceUrl;

    public OrderServiceImpl(OrderRepository orderRepository, RestTemplate restTemplate, UserRepository userRepository, RabbitTemplate rabbitTemplate, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        //Construccion de endpoint para consultar producto en el DataService
        String productEndpoint = dataServiceUrl + "/api/products/" + orderDTO.getProductId();

        //Verifico que el User exista mediante su ID
        userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        //Error cuando el prodcut no existe
        ProductoDTO product = restTemplate.getForObject(productEndpoint, ProductoDTO.class);
        if (product == null) {
            throw new ProductNotFoundException("Prodcut not found");
        }

        //Convertir DTO recibido a una entidad Order
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setStatus(OrderStatus.PENDING);
        //Guardo entiendad y mapeo de nuevo a DTO para retornar
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO findByIdOrder(Long id) {
        return orderRepository.findById(id)
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDTO> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Override
    public OrderDTO processOrder(Long orderId) {
        //Obtengo la orden
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        //Evito procesar ordenes que no esten Pendientes
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order already processed");
        }

        //Marco la orden a processing y guardo
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        //Enviamos el mensaje de actualización de manera Asíncrona a RabbitMQ para actualizar el stock
        StockUpdateMessage message = StockUpdateMessage.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_QUEUE, message);

        //Esperamos mediante el mecanismo asingcrono, una confirmacion que acutalice el estado
        return modelMapper.map(order, OrderDTO.class);
    }

}
