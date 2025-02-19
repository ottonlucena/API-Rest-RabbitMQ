package com.application.dataService.SpringBootRest.data.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STOCK_QUEUE = "stockQueue";
    public static final String STOCK_CONFIRMATION_QUEUE = "stockConfirmationQueue";

    @Bean
    public Queue stockQueue(){
        return new Queue(STOCK_QUEUE, true);
    }

    @Bean
    public Queue stockConfirmationQueue(){
        return new Queue(STOCK_CONFIRMATION_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
