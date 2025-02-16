package com.application.orderService.SpringBootRest.order.repository;

import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);
}
