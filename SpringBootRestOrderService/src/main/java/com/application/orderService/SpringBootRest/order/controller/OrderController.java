package com.application.orderService.SpringBootRest.order.controller;

import com.application.orderService.SpringBootRest.order.entities.Order;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return orderService.findByIdOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<Order> processOrder(@PathVariable Long id){
        Order processOrder = orderService.processOrder(id);
        return ResponseEntity.ok(processOrder);
    }

}
