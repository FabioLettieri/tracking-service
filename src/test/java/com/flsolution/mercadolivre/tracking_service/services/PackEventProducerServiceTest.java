package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;

@ExtendWith(MockitoExtension.class)
class PackEventProducerServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PackEventProducerService packEventProducerService;

    @Test
    void testSendPackEvent_whenValidRequest_thenSendMessageSuccessfully() throws Exception {
        PackEventRequestDTO requestDTO = PackEventRequestDTO.builder()
        		.description("Pacote chegou ao centro de distribuição")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location 1")
        		.packId(1L)
        		.build();

        String result = packEventProducerService.sendPackEvent(requestDTO);

        assertEquals("Message sent", result);
        verify(rabbitTemplate, times(1)).convertAndSend(
            eq(RabbitMQConfig.TRACKING_EVENTS_EXCHANGE),
            eq(RabbitMQConfig.TRACKING_EVENTS_ROUTING_KEY),
            any(PackEventRequestDTO.class) // Adicionando a tipagem explícita
        );
    }

    @Test
    void testSendPackEvent_whenExceptionOccurs_thenThrowException() {
    	
    	PackEventRequestDTO requestDTO = PackEventRequestDTO.builder()
        		.description("Pacote saiu para entrega")
        		.eventDateTime(LocalDateTime.now())
        		.location("Em rota")
        		.packId(2L)
        		.build();
    	
        doThrow(new RuntimeException("Falha ao enviar mensagem")).when(rabbitTemplate)
            .convertAndSend(eq(RabbitMQConfig.TRACKING_EVENTS_EXCHANGE), eq(RabbitMQConfig.TRACKING_EVENTS_ROUTING_KEY), any(PackEventRequestDTO.class));

        Exception exception = assertThrows(Exception.class, () -> packEventProducerService.sendPackEvent(requestDTO));

        assertEquals("Falha ao enviar mensagem", exception.getMessage());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(PackEventRequestDTO.class));
    }

    @Test
    void testSendListPackEvent_whenValidRequests_thenSendAllMessages() {
        PackEventRequestDTO request1 = PackEventRequestDTO.builder()
        		.description("Pacote chegou ao centro de distribuição")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location 1")
        		.packId(1L)
        		.build();

        PackEventRequestDTO request2 = PackEventRequestDTO.builder()
        		.description("Pacote saiu para entrega")
        		.eventDateTime(LocalDateTime.now())
        		.location("Em rota")
        		.packId(2L)
        		.build();

        List<PackEventRequestDTO> requests = Arrays.asList(request1, request2);

        String result = packEventProducerService.sendListPackEvent(requests);

        assertEquals("message sent", result);
        verify(rabbitTemplate, times(2)).convertAndSend(
            eq(RabbitMQConfig.TRACKING_EVENTS_EXCHANGE),
            eq(RabbitMQConfig.TRACKING_EVENTS_ROUTING_KEY),
            any(PackEventRequestDTO.class)
        );
    }

    @Test
    void testSendListPackEvent_whenOneMessageFails_thenContinueSendingOthers() {
    	PackEventRequestDTO request1 = PackEventRequestDTO.builder()
        		.description("Pacote chegou ao centro de distribuição")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location 1")
        		.packId(1L)
        		.build();

        PackEventRequestDTO request2 = PackEventRequestDTO.builder()
        		.description("Pacote saiu para entrega")
        		.eventDateTime(LocalDateTime.now())
        		.location("Em rota")
        		.packId(2L)
        		.build();

        List<PackEventRequestDTO> requests = Arrays.asList(request1, request2);

        doThrow(new RuntimeException("Erro ao enviar mensagem")).when(rabbitTemplate)
            .convertAndSend(eq(RabbitMQConfig.TRACKING_EVENTS_EXCHANGE),
                            eq(RabbitMQConfig.TRACKING_EVENTS_ROUTING_KEY),
                            eq(request2));

        String result = packEventProducerService.sendListPackEvent(requests);

        assertEquals("message sent", result);
        verify(rabbitTemplate, times(2)).convertAndSend(anyString(), anyString(), any(PackEventRequestDTO.class));
    }

    @Test
    void testSendListPackEvent_whenEmptyList_thenDoNothing() {
        List<PackEventRequestDTO> emptyList = List.of();

        String result = packEventProducerService.sendListPackEvent(emptyList);

        assertEquals("message sent", result);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(PackEventRequestDTO.class));
    }
}
