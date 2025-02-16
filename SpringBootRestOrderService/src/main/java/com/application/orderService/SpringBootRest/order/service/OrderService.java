package com.application.orderService.SpringBootRest.order.service;

import com.application.orderService.SpringBootRest.order.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);

    Optional<Order> findByIdOrder(Long id);

    List<Order> findAllOrders();

    Order processOrder(Long orderId);
}
