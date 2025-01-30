package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class ETagServiceTest {

    @InjectMocks
    private ETagService eTagService;

    @Mock
    private HttpServletRequest request;

    @Test
    void testGenerateETag_whenObjectProvided_thenReturnHashedString() {
        Object testObject = "TestString";
        String eTag = eTagService.generateETag(testObject);

        assertNotNull(eTag);
        assertEquals(String.valueOf(testObject.hashCode()), eTag);
    }

    @Test
    void testIsNotModified_whenETagMatches_thenReturnTrue() {
        String eTag = "12345";
        when(request.getHeader(HttpHeaders.IF_NONE_MATCH)).thenReturn("12345");

        boolean result = eTagService.isNotModified(request, eTag);

        assertTrue(result, "Se o ETag do cabeçalho for igual ao esperado, deve retornar verdadeiro.");
    }

    @Test
    void testIsNotModified_whenETagDoesNotMatch_thenReturnFalse() {
        String eTag = "12345";
        when(request.getHeader(HttpHeaders.IF_NONE_MATCH)).thenReturn("67890");

        boolean result = eTagService.isNotModified(request, eTag);

        assertFalse(result, "Se o ETag do cabeçalho for diferente do esperado, deve retornar falso.");
    }

    @Test
    void testIsNotModified_whenHeaderIsNull_thenReturnFalse() {
        String eTag = "12345";
        when(request.getHeader(HttpHeaders.IF_NONE_MATCH)).thenReturn(null);

        boolean result = eTagService.isNotModified(request, eTag);

        assertFalse(result, "Se o cabeçalho If-None-Match estiver ausente, deve retornar falso.");
    }
}
