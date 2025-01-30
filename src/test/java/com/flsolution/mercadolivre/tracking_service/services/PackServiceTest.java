package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackNotFoundException;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;
import com.flsolution.mercadolivre.tracking_service.utils.PackValidation;
import com.github.dockerjava.api.exception.BadRequestException;

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
    
    @Mock
    private PackEventHelperService packEventHelperService;

    @Mock
    private PackHelperService packHelperService;
    
    @Test
    void testShouldCreatePackSuccessfully() {
        PackRequestDTO requestDTO = new PackRequestDTO(
            "Livros para entrega",
            "Loja ABC",
            "João Silva",
            true,
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

        assertThrows(PackNotFoundException.class, () -> packService.updateStatusPack(1L, PackageStatus.IN_TRANSIT));

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
    
    @Test
    void testGetPackByIdAndIncludeEvents_whenPackExists_thenReturnResponseWithEvents() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.IN_TRANSIT);

        Pageable pageable = PageRequest.of(0, 10);
        List<PackEvent> eventsList = List.of(new PackEvent(), new PackEvent());
        Page<PackEvent> eventsPage = new PageImpl<>(eventsList, pageable, eventsList.size());

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packEventHelperService.findByPackId(1L, pageable)).thenReturn(eventsPage);

        PackResponseDTO result = packService.getPackByIdAndIncludeEvents(1L, true, pageable);

        assertNotNull(result);
        assertEquals(2, result.getEvents().size());
        assertEquals(PackageStatus.IN_TRANSIT, result.getStatus());
    }

    @Test
    void testGetPackByIdAndIncludeEvents_whenPackDoesNotExist_thenThrowException() {
        Pageable pageable = PageRequest.of(0, 10);

        when(packRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PackNotFoundException.class, () -> packService.getPackByIdAndIncludeEvents(1L, true, pageable));

        verify(packEventHelperService, never()).findByPackId(any(), any());
    }

    @Test
    void testGetPacks_whenCalled_thenReturnPaginatedList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PackResponseDTO> packList = List.of(new PackResponseDTO(), new PackResponseDTO());
        Page<PackResponseDTO> packPage = new PageImpl<>(packList, pageable, packList.size());

        when(packHelperService.getPackEvents(any(), any(), any())).thenReturn(packPage);

        Page<PackResponseDTO> result = packService.getPacks("Loja ABC", "João Silva", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void testGetPacks_whenNoResults_thenReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PackResponseDTO> emptyPage = Page.empty(pageable);

        when(packHelperService.getPackEvents(any(), any(), any())).thenReturn(emptyPage);

        Page<PackResponseDTO> result = packService.getPacks("Loja XYZ", "Maria Souza", pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCancelPack_whenPackExistsAndIsEligible_thenUpdateStatusToCancelled() throws BadRequestException, IOException {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.CREATED);

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));
        when(packRepository.save(any())).thenReturn(pack);

        PackCancelResponseDTO result = packService.cancelPack(1L);

        assertNotNull(result);
        assertEquals(PackageStatus.CANCELLED, pack.getStatus());
        verify(packRepository, times(1)).save(pack);
    }

    @Test
    void testCancelPack_whenPackNotFound_thenThrowException() {
        when(packRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PackNotFoundException.class, () -> packService.cancelPack(1L));

        verify(packRepository, never()).save(any(Pack.class));
    }

    @Test
    void testCancelPack_whenPackIsNotEligibleForCancellation_thenThrowBadRequestException() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.DELIVERED);

        when(packRepository.findById(1L)).thenReturn(Optional.of(pack));

        try (var mockedValidation = Mockito.mockStatic(PackValidation.class)) {
            mockedValidation.when(() -> PackValidation.validatePackElegibleForCancellation(PackageStatus.DELIVERED))
                            .thenThrow(new BadRequestException("O pacote não pode ser cancelado"));

            assertThrows(BadRequestException.class, () -> packService.cancelPack(1L));

            verify(packRepository, never()).save(any(Pack.class));
        }
    }
    @Test
    void testGetCacheControl_shouldReturnCorrectCacheSettings() {
        CacheControl cacheControl = packService.getCacheControl();

        assertNotNull(cacheControl);
        assertEquals("max-age=300", cacheControl.getHeaderValue());
        
    }
    
}
