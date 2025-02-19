package com.application.orderService.SpringBootRest.order.dto;

import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
