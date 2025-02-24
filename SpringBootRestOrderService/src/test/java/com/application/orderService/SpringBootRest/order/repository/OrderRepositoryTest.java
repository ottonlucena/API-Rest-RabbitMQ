package com.application.orderService.SpringBootRest.order.repository;

import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByStatus should return orders with the given status")
    public void testFindByStatus() {

        User user = User.builder()
                .name("test")
                .email("test@gmail.com")
                .build();
        user = entityManager.persistAndFlush(user);

        Order order1 = Order.builder()
                .user(user)
                .productId(2L)
                .quantity(5)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .user(user)
                .productId(3L)
                .quantity(2)
                .status(OrderStatus.COMPLETED)
                .build();

        Order order3 = Order.builder()
                .user(user)
                .productId(2L)
                .quantity(3)
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

        assertNotNull(pendingOrders);
        assertEquals(2, pendingOrders.size());
        pendingOrders.forEach(order -> assertEquals(OrderStatus.PENDING, order.getStatus()));

    }

}
