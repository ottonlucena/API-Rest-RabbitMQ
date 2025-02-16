package com.application.orderService.SpringBootRest.order.config.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTO {
        private Long id;
        private String name;
        private Double price;
        private Integer stock;


}
