package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventProducerServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackEventProducerService implements PackEventProducerServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(PackEventProducerService.class);
	
	private final RabbitTemplate rabbitTemplate;
	
	@Override
	public String sendPackEvent(PackEventRequestDTO requestDTO) throws Exception {
		logger.info("[START] - sendPackEvent() requestDTO: {}", requestDTO);
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.TRACKING_EVENTS_EXCHANGE, 
            RabbitMQConfig.TRACKING_EVENTS_ROUTING_KEY, 
            requestDTO
        );

        logger.info("[FINISH] - sendPackEvent()");
        return "Message sent";

		
	}

}
