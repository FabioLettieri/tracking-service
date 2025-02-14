package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;

@ExtendWith(MockitoExtension.class)
class PackEventServiceTest {

    @Mock
    private PackEventHelperService packEventHelperService;

    @Mock
    private PackEventRepository packEventRepository;

    @Mock
    private PackService packService;

    @InjectMocks
    private PackEventService packEventService;

    private Pageable pageable = PageRequest.of(0, 5);

    private PackEvent createPackEvent(Long id, Pack pack) {
        PackEvent packEvent = new PackEvent();
        packEvent.setId(id);
        packEvent.setPack(pack);
        return packEvent;
    }

    private Pack createPack(Long id) {
        Pack pack = new Pack();
        pack.setId(id);
        return pack;
    }
    
    @Test
    void testFindByPackId_whenValidId_thenReturnEvents() {
        Long packId = 1L;
        Pack pack = createPack(packId);
        PackEvent event1 = createPackEvent(10L, pack);
        PackEvent event2 = createPackEvent(11L, pack);
        Page<PackEvent> page = new PageImpl<>(List.of(event1, event2), pageable, 2);

        when(packEventHelperService.findByPackId(packId, pageable)).thenReturn(page);

        Page<PackEvent> result = packEventService.findByPackId(packId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testFindByPackId_whenNoEventsFound_thenReturnEmptyPage() {
        Long packId = 2L;
        when(packEventHelperService.findByPackId(packId, pageable)).thenReturn(Page.empty());

        Page<PackEvent> result = packEventService.findByPackId(packId, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testFindByPackId_whenNullId_thenReturnEmptyPage() {
        when(packEventHelperService.findByPackId(null, pageable)).thenReturn(Page.empty());

        Page<PackEvent> result = packEventService.findByPackId(null, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetPackEvents_whenEventsExist_thenReturnDTOs() {
    	PackEventDTO dto1 = PackEventDTO.builder()
    			.description("Event 1")
    			.eventDateTime(LocalDateTime.now())
    			.id(1L)
    			.location("Location 1")
    			.packId(1L)
    			.build();
    	
        PackEventDTO dto2 = PackEventDTO.builder()
    			.description("Event 2")
    			.eventDateTime(LocalDateTime.now())
    			.id(2L)
    			.location("Location 2")
    			.packId(2L)
    			.build();
        
        Page<PackEventDTO> dtoPage = new PageImpl<>(List.of(dto1, dto2), pageable, 2);

        when(packEventHelperService.getPackEvents(pageable)).thenReturn(dtoPage);

        Page<PackEventDTO> result = packEventService.getPackEvents(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetPackEvents_whenNoEvents_thenReturnEmptyPage() {
        when(packEventHelperService.getPackEvents(pageable)).thenReturn(Page.empty());

        Page<PackEventDTO> result = packEventService.getPackEvents(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testCreatePackEvent_whenPackNotFound_thenThrowException() {
        PackEventRequestDTO requestDTO = new PackEventRequestDTO(null, null, null, null);
        when(packService.getPackById(requestDTO.packId())).thenThrow(new RuntimeException("Pack not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> packEventService.createPackEvent(requestDTO));

        assertEquals("Pack not found", exception.getMessage());
    }

    @Test
    void testCreatePackEvent_whenSaveFails_thenThrowException() {
        Pack pack = createPack(1L);
        PackEventRequestDTO requestDTO = PackEventRequestDTO.builder()
        		.description("Event 1")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location 1")
        		.packId(1L)
        		.build();

        when(packService.getPackById(requestDTO.packId())).thenReturn(pack);
        when(packEventRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> packEventService.createPackEvent(requestDTO));

        assertEquals("Database error", exception.getMessage());
    
    }
 
    @Test
    void testGetCacheControl_shouldReturnCorrectCacheDuration() {
        CacheControl cacheControl = packEventService.getCacheControl();

        assertNotNull(cacheControl);
        assertEquals("max-age=300", cacheControl.getHeaderValue());
    }
}
