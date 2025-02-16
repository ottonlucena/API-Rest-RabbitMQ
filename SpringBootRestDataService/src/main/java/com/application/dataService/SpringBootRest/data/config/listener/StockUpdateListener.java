package com.application.dataService.SpringBootRest.data.config.listener;

import com.application.dataService.SpringBootRest.data.config.messaging.StockUpdateMessage;
import com.application.dataService.SpringBootRest.data.entities.Product;
import com.application.dataService.SpringBootRest.data.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockUpdateListener {

    private final ProductRepository productRepository;

    public StockUpdateListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = "stockQueue")
    public void handleStockUpdate(StockUpdateMessage message){
        Long productId = message.getProductId();
        Integer quantity = message.getQuantity();

        //Busco el producto en la bbdd
        Product product = productRepository.findById(productId)
                .orElse(null);

        if (product != null){
            int newStock = product.getStock() - quantity;
            if(newStock < 0){
                System.out.println("Stock insuficiente");
                return;
            }
            product.setStock(newStock);
            productRepository.save(product);
            System.out.println("Stock actualizado para el producto " + productId);
        }else {
            System.out.println("Producto no encontrado para el id " + productId);
        }
    }

}
