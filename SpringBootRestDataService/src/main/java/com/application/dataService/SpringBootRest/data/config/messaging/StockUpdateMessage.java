package com.application.dataService.SpringBootRest.data.config.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateMessage  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long productId;
    private Integer quantity;

}
