package com.application.dataService.SpringBootRest.data.exception;

public class ProductOutOfStockException extends  RuntimeException{

    public ProductOutOfStockException(String message){
        super(message);
    }

}
