package com.flsolution.mercadolivre.tracking_service.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    public static final String TRACKING_EVENTS_QUEUE = "tracking-events-queue";
    public static final String TRACKING_EVENTS_EXCHANGE = "tracking-events-exchange";
    public static final String TRACKING_EVENTS_ROUTING_KEY = "tracking.events";

    @Bean
    Queue trackingEventsQueue() {
    	logger.info("[START] - trackingEventsQueue()");

    	Queue response = new Queue(TRACKING_EVENTS_QUEUE, true);
        
    	logger.info("[FINISH] - trackingEventsQueue()");
    	return response;
    }

    @Bean
    DirectExchange trackingEventsExchange() {
    	logger.info("[START] - trackingEventsExchange()");

    	DirectExchange response = new DirectExchange(TRACKING_EVENTS_EXCHANGE);
        
    	logger.info("[FINISH] - trackingEventsExchange()");
    	return response;
    }

    @Bean
    Binding trackingEventsBinding(Queue trackingEventsQueue, DirectExchange trackingEventsExchange) {
    	logger.info("[START] - trackingEventsBinding() trackingEventsQueue: {}, trackingEventsExchange: {}", trackingEventsQueue, trackingEventsExchange);
    	Binding response = BindingBuilder.bind(trackingEventsQueue).to(trackingEventsExchange).with(TRACKING_EVENTS_ROUTING_KEY);
    	
    	logger.info("[FINISH] - trackingEventsBinding()");
        return response;
    }

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        logger.info("[START] - jsonMessageConverter()");
        
        Jackson2JsonMessageConverter response = new Jackson2JsonMessageConverter();
        
        logger.info("[FINISH] - jsonMessageConverter()");
        return response;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        logger.info("[START] - rabbitTemplate() messageConverter: {}", messageConverter);
        
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        
        logger.info("[FINISH] - rabbitTemplate()");
        return rabbitTemplate;
    }
}
