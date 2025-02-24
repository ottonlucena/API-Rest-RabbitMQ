package com.application.orderService.SpringBootRest.order.service.Impl;

import com.application.orderService.SpringBootRest.order.config.RabbitMQConfig;
import com.application.orderService.SpringBootRest.order.config.messaging.StockUpdateMessage;
import com.application.orderService.SpringBootRest.order.dto.OrderDTO;
import com.application.orderService.SpringBootRest.order.dto.ProductoDTO;
import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.entities.User;
import com.application.orderService.SpringBootRest.order.repository.OrderRepository;
import com.application.orderService.SpringBootRest.order.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDTO orderDTO;
    private User user;

    @BeforeEach
    public void setUp() {

        //COnfiguro un valor para dataServiceUrl (ReflectionTestUtils)
        ReflectionTestUtils.setField(orderService, "dataServiceUrl", "httphttp://localhost:8090");

        user = user.builder()
                .id(1L)
                .name("Test")
                .email("test@gmail.com")
                .build();

        //Configuro objeto de prueba
        order = order.builder()
                .id(1L)
                .productId(2L)
                .status(OrderStatus.COMPLETED)
                .quantity(3)
                .build();

        orderDTO = orderDTO.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .status(OrderStatus.COMPLETED)
                .quantity(3)
                .build();
    }

    @Test
    public void testCreateOrderSuccess() {
        //Hago simulacion de usuario
        when(userRepository.findById(orderDTO.getUserId())).thenReturn(Optional.of(user));
        //Creo un producto simulado
        ProductoDTO productoDTO = ProductoDTO.builder()
                .id(orderDTO.getProductId())
                .build();
        //Configuracion de mock para el restTemplate y retorne el productDTO
        when(restTemplate.getForObject(anyString(), eq(ProductoDTO.class))).thenReturn(productoDTO);

        //Mapeo de DTO a entidad
        when(modelMapper.map(orderDTO, Order.class)).thenReturn(order);
        //Hago simulacion de guardar order
        when(orderRepository.save(order)).thenReturn(order);
        //Mapeo de entidad a DTO para retorno
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result, "El resultado no debe ser nulo");
        System.out.println(result);
        assertEquals(orderDTO.getId(), result.getId(), "El ID de la orden debe coincidir");
        verify(userRepository).findById(orderDTO.getUserId());
        verify(orderRepository).save(order);


    }

    @Test
    public void TestFindByOrderSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO foundOrder = orderService.findByIdOrder(1L);

        assertNotNull(foundOrder, "La orden encontrada no debe ser nula");
        assertEquals(orderDTO.getId(), foundOrder.getId(), "El ID de la orden debe coincidir");
        verify(orderRepository).findById(1L);
    }

    @Test
    public void testFindAllOrders() {
        //Arrange
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        // Act

        List<OrderDTO> orders = orderService.findAllOrders();

        // Assert
        assertNotNull(orders, "La lista de órdenes no debe ser nula");
        assertEquals(1, orders.size(), "Debe haber exactamente una orden");
        assertEquals(orderDTO.getId(), orders.get(0).getId(), "El ID de la orden debe coincidir");
        verify(orderRepository).findAll();
    }

    @Test
    public void testProcessOrderSuccess() {
        //Aseguro que la orden este en estatus PENDING
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO processedOrder = orderService.processOrder(order.getId());

        assertNotNull(processedOrder, "El DTO de la orden procesada no debe ser nulo");
        System.out.println(processedOrder);
        assertEquals(OrderStatus.PROCESSING, order.getStatus(), "El estado de la orden debe ser PENDING");
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.STOCK_QUEUE), any(StockUpdateMessage.class));
        verify(orderRepository).save(order);
    }

    @Test
    public void testProcessOrderAlreadyProcessed() {
        order.setStatus(OrderStatus.COMPLETED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.processOrder(order.getId()));
        verify(orderRepository).findById(order.getId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(rabbitTemplate, never()).convertAndSend(anyString());
    }


}
