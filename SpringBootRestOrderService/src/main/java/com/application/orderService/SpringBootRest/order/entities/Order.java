package com.application.orderService.SpringBootRest.order.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;

    @Column(name = "product_id")
    @NotNull(message = "Enter a product")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Enter a quantity")
    @Positive(message = "The quantity must be positive")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
