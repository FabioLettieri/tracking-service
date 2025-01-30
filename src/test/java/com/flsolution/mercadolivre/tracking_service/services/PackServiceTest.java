package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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

        when(apiNagerService.isHoliday("24/10/2025")).thenReturn(true);
        when(apiTheDogService.getFunFact()).thenReturn("Dogs are cool!");
        when(packRepository.save(any(Pack.class)))
            .thenReturn(new Pack("Livros para entrega", "Loja ABC", "João Silva", true, "Dogs are cool!", null, PackageStatus.CREATED));

        PackResponseDTO result = packService.createPack(requestDTO);

        assertNotNull(result);
        assertEquals("Livros para entrega", result.getDescription());
        assertEquals("Loja ABC", result.getSender());
    }
    
    @Test
    void testValidStatusTransition_whenUpdateStatus_thenUpdateSuccessfully() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.CREATED);

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packRepository.save(any())).thenReturn(pack);

        PackResponseDTO result = packService.updateStatusPack(1L, PackageStatus.IN_TRANSIT);

        assertEquals(PackageStatus.IN_TRANSIT, result.getStatus());
        verify(packRepository, times(1)).save(pack);
    }
    
    @Test
    void testUpdateStatusPack_whenPackNotFound_thenThrowNotFoundException() {
        when(packRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> packService.updateStatusPack(1L, PackageStatus.IN_TRANSIT));

        verify(packRepository, never()).save(any(Pack.class));
    }


    @Test
    void testIsValidTransition_whenTransitionValid_thenReturnTrue() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.CREATED); 

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packRepository.save(any(Pack.class))).thenReturn(pack);

        PackResponseDTO result = packService.updateStatusPack(1L, PackageStatus.IN_TRANSIT);

        assertNotNull(result);
        assertEquals(PackageStatus.IN_TRANSIT, result.getStatus());
        verify(packRepository, times(1)).save(any(Pack.class));
    }

    @Test
    void testUpdateStatusPack_whenTransitionToDelivered_thenSetDeliveredAt() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.IN_TRANSIT);

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packRepository.save(any(Pack.class))).thenReturn(pack);

        PackResponseDTO result = packService.updateStatusPack(1L, PackageStatus.DELIVERED);

        assertNotNull(result);
        assertEquals(PackageStatus.DELIVERED, result.getStatus());
        assertNotNull(result.getDeliveredAt());
        verify(packRepository, times(1)).save(any(Pack.class));
    }

    @Test
    void testUpdateStatusPack_whenValidTransition_thenReturnCorrectResponse() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setDescription("Pacote de teste");
        pack.setSender("Loja ABC");
        pack.setRecipient("João Silva");
        pack.setStatus(PackageStatus.CREATED);

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packRepository.save(any(Pack.class))).thenReturn(pack);

        PackResponseDTO result = packService.updateStatusPack(1L, PackageStatus.IN_TRANSIT);

        assertNotNull(result);
        assertEquals("Pacote de teste", result.getDescription());
        assertEquals("Loja ABC", result.getSender());
        assertEquals("João Silva", result.getRecipient());
        assertEquals(PackageStatus.IN_TRANSIT, result.getStatus());
        verify(packRepository, times(1)).save(any(Pack.class));
    }
    
   

    
}
