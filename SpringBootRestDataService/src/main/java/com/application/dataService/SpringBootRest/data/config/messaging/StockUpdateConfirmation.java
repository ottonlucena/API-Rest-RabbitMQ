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
public class StockUpdateConfirmation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private boolean success;
    private String message;

}
