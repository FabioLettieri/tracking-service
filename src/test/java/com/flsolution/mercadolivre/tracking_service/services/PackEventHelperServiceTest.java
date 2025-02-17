package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.flsolution.mercadolivre.tracking_service.converters.PackEventConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;

@ExtendWith(MockitoExtension.class)
class PackEventHelperServiceTest {

    @Mock
    private PackEventRepository packEventRepository;

    @Mock
    private PackEventConverter packEventConverter;

    @InjectMocks
    private PackEventHelperService packEventHelperService;

    private PackEvent createPackEvent(Long eventId, Long packId) {
        Pack pack = new Pack();
        pack.setId(packId);

        PackEvent event = new PackEvent();
        event.setId(eventId);
        event.setPack(pack);

        return event;
    }

    @Test
    void testFindByPackId_whenValidId_thenReturnEvents() {
        Long packId = 1L;
        Pageable pageable = PageRequest.of(0, 5);
        PackEvent event1 = createPackEvent(10L, packId);
        PackEvent event2 = createPackEvent(11L, packId);
        List<PackEvent> events = List.of(event1, event2);
        Page<PackEvent> page = new PageImpl<>(events, pageable, events.size());

        when(packEventRepository.findByPackId(packId, pageable)).thenReturn(page);

        Page<PackEvent> result = packEventHelperService.findByPackId(packId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(event1.getId(), result.getContent().get(0).getId());
    }

    @Test
    void testFindByPackId_whenNoEventsFound_thenReturnEmptyPage() {
        Long packId = 2L;
        Pageable pageable = PageRequest.of(0, 5);

        when(packEventRepository.findByPackId(packId, pageable)).thenReturn(Page.empty());

        Page<PackEvent> result = packEventHelperService.findByPackId(packId, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testFindByPackId_whenInvalidId_thenReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);

        when(packEventRepository.findByPackId(null, pageable)).thenReturn(Page.empty());
        when(packEventRepository.findByPackId(-1L, pageable)).thenReturn(Page.empty());

        Page<PackEvent> resultForNullId = packEventHelperService.findByPackId(null, pageable);
        Page<PackEvent> resultForNegativeId = packEventHelperService.findByPackId(-1L, pageable);

        assertNotNull(resultForNullId);
        assertEquals(0, resultForNullId.getTotalElements());

        assertNotNull(resultForNegativeId);
        assertEquals(0, resultForNegativeId.getTotalElements());
    }

    @Test
    void testGetPackEvents_whenNoEvents_thenReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);

        when(packEventRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<PackEventResponse> result = packEventHelperService.getPackEvents(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetPackEvents_whenConversionFails_thenReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);
        PackEvent event1 = createPackEvent(10L, 1L);
        event1.setEventDateTime(LocalDateTime.now());
        List<PackEvent> events = List.of(event1);
        Page<PackEvent> page = new PageImpl<>(events, pageable, events.size());

        when(packEventRepository.findAll(pageable)).thenReturn(page);

        Page<PackEventResponse> result = packEventHelperService.getPackEvents(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
