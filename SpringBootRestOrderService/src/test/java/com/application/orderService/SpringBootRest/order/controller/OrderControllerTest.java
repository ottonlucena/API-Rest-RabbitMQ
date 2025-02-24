package com.application.orderService.SpringBootRest.order.controller;

import com.application.orderService.SpringBootRest.order.dto.OrderDTO;
import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.application.orderService.SpringBootRest.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    public void testGetAllOrders() throws Exception {

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .userId(1L)
                .productId(2L)
                .status(OrderStatus.COMPLETED)
                .quantity(3)
                .build();
        Mockito.when(orderService.findAllOrders()).thenReturn(Arrays.asList(orderDTO));
        System.out.println(orderDTO);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderDTO.getId()))
                .andExpect(jsonPath("$[0].user_id").value(orderDTO.getUserId()))
                .andExpect(jsonPath("$[0].product_id").value(orderDTO.getProductId()))
                .andExpect(jsonPath("$[0].status").value(orderDTO.getStatus().toString()))
                .andExpect(jsonPath("$[0].quantity").value(orderDTO.getQuantity()));


    }

    @Test
    public void testCreateOrder() throws Exception {

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .userId(1L)
                .productId(2L)
                .status(OrderStatus.COMPLETED)
                .quantity(3)
                .build();
        Mockito.when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        String orderJson = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderDTO.getId()));

    }

    @Test
    public void testProcessOrder() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .userId(1L)
                .productId(2L)
                .status(OrderStatus.COMPLETED)
                .quantity(3)
                .build();
        Mockito.when(orderService.processOrder(1L)).thenReturn(orderDTO);

        mockMvc.perform(put("/api/orders/1/process"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDTO.getId()))
                .andExpect(jsonPath("$.status").value(orderDTO.getStatus().toString()));

    }


}
