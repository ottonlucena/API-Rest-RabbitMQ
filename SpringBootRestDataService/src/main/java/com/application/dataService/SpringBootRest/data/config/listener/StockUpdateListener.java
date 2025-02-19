package com.application.dataService.SpringBootRest.data.config.listener;

import com.application.dataService.SpringBootRest.data.config.RabbitMQConfig;
import com.application.dataService.SpringBootRest.data.config.messaging.StockUpdateConfirmation;
import com.application.dataService.SpringBootRest.data.config.messaging.StockUpdateMessage;
import com.application.dataService.SpringBootRest.data.entities.Product;
import com.application.dataService.SpringBootRest.data.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class StockUpdateListener {

    private static final Logger logger = LoggerFactory.getLogger(StockUpdateListener.class);

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public StockUpdateListener(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
    public void handleStockUpdate(StockUpdateMessage message){
        Long productId = message.getProductId();
        int quantity = message.getQuantity();
        StockUpdateConfirmation confirmation;

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null){
            confirmation = StockUpdateConfirmation.builder()
                    .orderId(message.getOrderId())
                    .success(false)
                    .message("Product not found")
                    .build();
        }else {
            int newStock = product.getStock() - quantity;
            if (newStock < 0){
                confirmation = StockUpdateConfirmation.builder()
                        .orderId(message.getOrderId())
                        .success(false)
                        .message("Insufficient stock")
                        .build();
            }else {
                product.setStock(newStock);
                productRepository.save(product);
                confirmation = StockUpdateConfirmation.builder()
                        .orderId(message.getOrderId())
                        .success(true)
                        .message("Stock updated successfully")
                        .build();
            }
        }
        logger.info("product {}", confirmation);
        //Publico el mensaje de confirmacion
        rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_CONFIRMATION_QUEUE, confirmation);

    }

}
