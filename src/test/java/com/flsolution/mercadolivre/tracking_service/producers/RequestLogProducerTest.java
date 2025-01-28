package com.flsolution.mercadolivre.tracking_service.producers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class RequestLogProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RequestLogProducer requestLogProducer;

    @Test
    void testSendMessageSuccessfully() throws Exception {
        String message = "{\"id\":1,\"description\":\"Test log\"}";

        requestLogProducer.sendMessage(message);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("request-log-queue"), eq(message));
    }

    @Test
    void testSendMessageWithException() {
        String message = "invalid_message";
        doThrow(new RuntimeException("RabbitMQ connection failed")).when(rabbitTemplate).convertAndSend(anyString(), anyString());

        Exception exception = assertThrows(Exception.class, () -> requestLogProducer.sendMessage(message));

        assertEquals(Exception.class, exception.getClass());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("request-log-queue"), eq(message));
    }
}
