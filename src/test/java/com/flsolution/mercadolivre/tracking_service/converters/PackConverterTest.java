package com.flsolution.mercadolivre.tracking_service.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackCancelResponse;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

class PackConverterTest {

    @Test
    void testToEntity() {
        PackRequest request = new PackRequest("Description", "Sender", "Recipient", true, "2025-10-24", 1L);

        Pack pack = PackConverter.toEntity(request);

        assertNotNull(pack);
        assertEquals(request.getDescription(), pack.getDescription());
        assertEquals(request.getSender(), pack.getSender());
        assertEquals(request.getRecipient(), pack.getRecipient());
        assertEquals(request.getIsHolliday(), pack.getIsHolliday());
        assertEquals(request.getEstimatedDeliveryDate(), pack.getEstimatedDeliveryDate());
        assertEquals(PackageStatus.CREATED, pack.getStatus());
    }

    @Test
    void testToResponseDTO() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setDescription("Description");
        pack.setSender("Sender");
        pack.setRecipient("Recipient");
        pack.setStatus(PackageStatus.CREATED);
        pack.setCreatedAt(LocalDateTime.now());
        pack.setUpdatedAt(LocalDateTime.now());
        pack.setDeliveredAt(LocalDateTime.now());
        pack.setEvents(new ArrayList<>());

        PackResponse response = PackConverter.toResponseDTO(pack);

        assertNotNull(response);
        assertEquals(pack.getId(), response.id());
        assertEquals(pack.getDescription(), response.description());
        assertEquals(pack.getSender(), response.sender());
        assertEquals(pack.getRecipient(), response.recipient());
        assertEquals(pack.getStatus(), response.status());
        assertEquals(pack.getCreatedAt(), response.createdAt());
        assertEquals(pack.getUpdatedAt(), response.updatedAt());
        assertEquals(pack.getDeliveredAt(), response.deliveredAt());
        assertEquals(0, response.events().size());
    }

    @Test
    void testListEventToResponseDTO() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setDescription("Description");
        pack.setSender("Sender");
        pack.setRecipient("Recipient");
        pack.setStatus(PackageStatus.CREATED);
        pack.setCreatedAt(LocalDateTime.now());
        pack.setUpdatedAt(LocalDateTime.now());
        pack.setDeliveredAt(LocalDateTime.now());

        List<PackEvent> eventList = new ArrayList<>();
        PackEvent event = new PackEvent();
        event.setId(1L);
        event.setLocation("Location");
        event.setDescription("Event Description");
        event.setEventDateTime(LocalDateTime.now());
        eventList.add(event);

        Page<PackEvent> eventPage = new PageImpl<>(eventList);

        PackResponse response = PackConverter.listEventToResponseDTO(pack, eventPage);

        assertNotNull(response);
        assertEquals(pack.getId(), response.id());
        assertEquals(pack.getDescription(), response.description());
        assertEquals(pack.getSender(), response.sender());
        assertEquals(pack.getRecipient(), response.recipient());
        assertEquals(pack.getStatus(), response.status());
        assertEquals(pack.getCreatedAt(), response.createdAt());
        assertEquals(pack.getUpdatedAt(), response.updatedAt());
        assertEquals(pack.getDeliveredAt(), response.deliveredAt());
        assertEquals(1, response.events().size());

        PackEventResponse eventResponse = response.events().get(0);
        assertEquals(event.getId(), eventResponse.id());
        assertEquals(pack.getId(), eventResponse.packId());
        assertEquals(event.getLocation(), eventResponse.location());
        assertEquals(event.getDescription(), eventResponse.description());
        assertEquals(event.getEventDateTime(), eventResponse.eventDateTime());
    }

    @Test
    void testToCancelResponseDTO() {
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setStatus(PackageStatus.CREATED);
        pack.setUpdatedAt(LocalDateTime.now());

        PackCancelResponse response = PackConverter.toCancelResponseDTO(pack);

        assertNotNull(response);
        assertEquals(pack.getId(), response.id());
        assertEquals(pack.getStatus(), response.status());
        assertEquals(pack.getUpdatedAt(), response.updatedAt());
    }

    @Test
    void testToListPackResponseDTO() {
        List<Pack> packList = new ArrayList<>();
        Pack pack = new Pack();
        pack.setId(1L);
        pack.setDescription("Description");
        pack.setSender("Sender");
        pack.setRecipient("Recipient");
        pack.setStatus(PackageStatus.CREATED);
        pack.setCreatedAt(LocalDateTime.now());
        pack.setUpdatedAt(LocalDateTime.now());
        pack.setDeliveredAt(LocalDateTime.now());
        packList.add(pack);

        Page<Pack> packPage = new PageImpl<>(packList);

        Page<PackResponse> responsePage = PackConverter.toListPackResponseDTO(packPage);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());

        PackResponse response = responsePage.getContent().get(0);
        assertEquals(pack.getId(), response.id());
        assertEquals(pack.getDescription(), response.description());
        assertEquals(pack.getSender(), response.sender());
        assertEquals(pack.getRecipient(), response.recipient());
        assertEquals(pack.getStatus(), response.status());
        assertEquals(pack.getCreatedAt(), response.createdAt());
        assertEquals(pack.getUpdatedAt(), response.updatedAt());
        assertEquals(pack.getDeliveredAt(), response.deliveredAt());
    }
}
