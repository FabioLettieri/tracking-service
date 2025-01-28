package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;

@ExtendWith(MockitoExtension.class)
class PackServiceTest {

    @Mock
    private PackRepository packRepository;

    @Mock
    private ExternalApiNagerService apiNagerService;

    @Mock
    private ExternalApiTheDogService apiTheDogService;

    @InjectMocks
    private PackService packService;

    @Test
    void testShouldCreatePackSuccessfully() {
        PackRequestDTO requestDTO = new PackRequestDTO(
            "Livros para entrega",
            "Loja ABC",
            "João Silva",
            true,
            "fun fact",
            "24/10/2025"
        );

        when(apiNagerService.isHolliday("24/10/2025")).thenReturn(true);
        when(apiTheDogService.getFunFact()).thenReturn("Dogs are cool!");
        when(packRepository.save(any(Pack.class)))
            .thenReturn(new Pack("Livros para entrega", "Loja ABC", "João Silva", true, "Dogs are cool!", null, PackageStatus.CREATED));

        PackResponseDTO result = packService.createPack(requestDTO);

        assertNotNull(result);
        assertEquals("Livros para entrega", result.getDescription());
        assertEquals("Loja ABC", result.getSender());
    }
}
