package com.flsolution.mercadolivre.tracking_service.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.flsolution.mercadolivre.tracking_service.converters.PackEventConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.request.PackEventRequest;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;
import com.flsolution.mercadolivre.tracking_service.services.PackService;

@ExtendWith(MockitoExtension.class)
class PackEventBatchProcessorTest {

	@InjectMocks
	private PackEventBatchProcessor packEventBatchProcessor;

	@Mock
	private RabbitTemplate rabbitTemplate;

	@Mock
	private PackEventRepository packEventRepository;

	@Mock
	private PackService packService;

	@Value("${batch.tracking-events.batch-size}")
	private int batchSize = 10;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testProcessBatch_withEvents() {
		List<PackEventRequest> events = new ArrayList<>();
		for (int i = 0; i < batchSize; i++) {
			PackEventRequest event = PackEventRequest.builder().description("Description" + i)
					.eventDateTime(LocalDateTime.now()).location("Location" + i).packId(i + 1L).build();

			events.add(event);
			lenient().when(rabbitTemplate.receiveAndConvert("tracking-events-queue")).thenReturn(event);
		}

		lenient().when(packService.getPackById(any())).thenReturn(new Pack());
		lenient().when(rabbitTemplate.receiveAndConvert("tracking-events-queue")).thenReturn(null);

		packEventBatchProcessor.processBatch();

		List<PackEvent> packEvents = events.stream().map(request -> {
			Pack pack = packService.getPackById(request.packId());
			return PackEventConverter.toEntity(request, pack);
		}).collect(Collectors.toList());

		packEventRepository.saveAll(packEvents);

		verify(packEventRepository, times(1)).saveAll(packEvents);
	}

	@Test
	void testProcessBatch_noEvents() {
		packEventBatchProcessor.processBatch();

		verify(packEventRepository, times(0)).saveAll(anyList());
	}
}
