package com.application.orderService.SpringBootRest.order.service;

import com.application.orderService.SpringBootRest.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO order);

    OrderDTO findByIdOrder(Long id);

    List<OrderDTO> findAllOrders();

    OrderDTO processOrder(Long orderId);
}
