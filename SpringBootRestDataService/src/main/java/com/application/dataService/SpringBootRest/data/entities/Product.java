package com.application.dataService.SpringBootRest.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @NotBlank(message = "Enter a name ")
    private String name;
    @NotNull(message = "Enter a price for the product")
    private Double price;
    @NotNull(message = "Enter a quantity")
    @Positive(message = "The quantity must be positive")
    private Integer stock;
}
