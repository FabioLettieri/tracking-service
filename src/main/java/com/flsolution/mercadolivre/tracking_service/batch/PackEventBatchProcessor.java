package com.flsolution.mercadolivre.tracking_service.batch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackNotFoundException;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;
import com.flsolution.mercadolivre.tracking_service.services.PackService;

import jakarta.transaction.Transactional;
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

    @Async("taskExecutor")
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

    @Transactional
    private void saveBatch(List<PackEventRequestDTO> events) {
        logger.info("[START] - Salvando lote de {} eventos", events.size());

        List<PackEvent> packEvents = new ArrayList<>();

        for (PackEventRequestDTO requestDTO : events) {
            try {
                PackEvent packEvent = new PackEvent();
                packEvent.setPack(packService.getPackById(requestDTO.packId()));
                packEvent.setLocation(requestDTO.location());
                packEvent.setDescription(requestDTO.description());
                packEvent.setEventDateTime(LocalDateTime.now());

                packEvents.add(packEvent);
            } catch (PackNotFoundException ex) {
                logger.warn("[WARNING] - Pack com ID {} não encontrado, evento ignorado.", requestDTO.packId());
                continue; 
            } catch (Exception ex) {
                logger.error("[ERROR] - Falha ao processar evento do pack ID {}: {}", requestDTO.packId(), ex.getMessage());
            }
        }

        if (!packEvents.isEmpty()) {
            packEventRepository.saveAll(packEvents);
            logger.info("[FINISH] - saveBatch() - {} eventos salvos no banco", packEvents.size());
        } else {
            logger.warn("[WARNING] - Nenhum evento válido foi salvo no batch.");
        }
    }
}
