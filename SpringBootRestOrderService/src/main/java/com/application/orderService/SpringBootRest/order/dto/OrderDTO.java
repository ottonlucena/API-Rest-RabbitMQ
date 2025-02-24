package com.application.orderService.SpringBootRest.order.dto;

import com.application.orderService.SpringBootRest.order.entities.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "Enter a product")
    @JsonProperty("product_id")
    private Long productId;

    @Positive(message = "The quantity must be positive")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
