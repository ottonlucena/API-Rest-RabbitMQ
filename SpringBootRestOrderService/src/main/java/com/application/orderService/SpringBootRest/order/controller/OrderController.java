package com.application.orderService.SpringBootRest.order.controller;

import com.application.orderService.SpringBootRest.order.dto.OrderDTO;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.findByIdOrder(id);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<OrderDTO> processOrder(@PathVariable Long id) {
        OrderDTO processOrder = orderService.processOrder(id);
        return ResponseEntity.ok(processOrder);
    }

}
