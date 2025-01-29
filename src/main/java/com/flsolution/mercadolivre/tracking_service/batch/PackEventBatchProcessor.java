package com.flsolution.mercadolivre.tracking_service.batch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;
import com.flsolution.mercadolivre.tracking_service.services.PackService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PackEventBatchProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PackEventBatchProcessor.class);

    private final RabbitTemplate rabbitTemplate;
    private final PackEventRepository packEventRepository;
    private final PackService packService;

    @Value("${batch.tracking-events.batch-size}")
    private int batchSize;

    @Scheduled(fixedRateString = "${batch.tracking-events.interval-ms}")
    public void processBatch() {
        logger.info("[START] - processBatch() - Consumindo eventos da fila");

        List<PackEventRequestDTO> events = new ArrayList<>();

        while (events.size() < batchSize) {
            PackEventRequestDTO event = (PackEventRequestDTO) rabbitTemplate.receiveAndConvert("tracking-events-queue");
            if (event == null) break;

            logger.info("[RECEIVED] processBatch() event: {}", event);
            events.add(event);
        }

        if (!events.isEmpty()) {
            saveBatch(events);
        }

        logger.info("[FINISH] - processBatch() - Processamento concluído");
    }

    private void saveBatch(List<PackEventRequestDTO> events) {
        logger.info("[START] - Salvando lote de {} eventos", events.size());

        List<PackEvent> packEvents = new ArrayList<>();

        for (PackEventRequestDTO requestDTO : events) {
            try {
                PackEvent packEvent = new PackEvent(); 
                packEvent.setPack(packService.getPackById(requestDTO.getPackId()));
                packEvent.setLocation(requestDTO.getLocation());
                packEvent.setDescription(requestDTO.getDescription());
                packEvent.setEventDateTime(requestDTO.getEventDateTime());

                packEvents.add(packEvent);
            } catch (Exception e) {
                logger.error("[FINISH] - saveBatch() - Falha ao processar evento: {}", e.getMessage(), e);
            }
        }

        if (!packEvents.isEmpty()) {
            packEventRepository.saveAll(packEvents);
            logger.info("[FINISH] - saveBatch() - {} eventos salvos no banco", packEvents.size());
        }
    }
}
