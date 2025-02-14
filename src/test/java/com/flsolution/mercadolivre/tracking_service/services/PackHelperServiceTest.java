package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;

@ExtendWith(MockitoExtension.class)
class PackHelperServiceTest {

    @Mock
    private PackRepository packRepository;

    @InjectMocks
    private PackHelperService packHelperService;

    private Pageable pageable = PageRequest.of(0, 5);

    private Pack createPack(Long id, String sender, String recipient) {
        Pack pack = new Pack();
        pack.setId(id);
        pack.setSender(sender);
        pack.setRecipient(recipient);
        return pack;
    }

    @Test
    void testGetPackEvents_whenSenderAndRecipientProvided_thenReturnFilteredResults() {
        String sender = "Loja ABC";
        String recipient = "João Silva";

        Pack pack1 = createPack(1L, sender, recipient);
        Pack pack2 = createPack(2L, sender, recipient);

        List<Pack> packList = List.of(pack1, pack2);
        Page<Pack> page = new PageImpl<>(packList, pageable, packList.size());

        when(packRepository.findBySenderAndRecipient(sender, recipient, pageable)).thenReturn(page);

        try (var mockedConverter = mockStatic(PackConverter.class)) {
            mockedConverter.when(() -> PackConverter.toResponseDTO(any(Pack.class)))
                           .thenAnswer(invocation -> PackResponseDTO.builder()
                        		   .createdAt(LocalDateTime.now())
                        		   .deliveredAt(LocalDateTime.now())
                        		   .description("Livros para entrega")
                        		   .events(new ArrayList<PackEventDTO>())
                        		   .id(1L)
                        		   .recipient(recipient)
                        		   .sender(sender)
                        		   .status(PackageStatus.CREATED)
                        		   .updatedAt(LocalDateTime.now())
                        		   .build()
                        		   );
            Page<PackResponseDTO> result = packHelperService.getPackEvents(sender, recipient, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    @Test
    void testGetPackEvents_whenOnlySenderProvided_thenReturnFilteredResults() {
        String sender = "Loja ABC";

        Pack pack1 = createPack(1L, sender, "Cliente A");
        Pack pack2 = createPack(2L, sender, "Cliente B");

        List<Pack> packList = List.of(pack1, pack2);
        Page<Pack> page = new PageImpl<>(packList, pageable, packList.size());

        when(packRepository.findBySender(sender, pageable)).thenReturn(page);

        try (var mockedConverter = mockStatic(PackConverter.class)) {
            mockedConverter.when(() -> PackConverter.toResponseDTO(any(Pack.class)))
                           .thenAnswer(invocation -> PackResponseDTO.builder()
                        		   .createdAt(LocalDateTime.now())
                        		   .deliveredAt(LocalDateTime.now())
                        		   .description("Livros para entrega")
                        		   .events(new ArrayList<PackEventDTO>())
                        		   .id(1L)
                        		   .recipient(null)
                        		   .sender(sender)
                        		   .status(PackageStatus.CREATED)
                        		   .updatedAt(LocalDateTime.now())
                        		   .build());

            Page<PackResponseDTO> result = packHelperService.getPackEvents(sender, null, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    @Test
    void testGetPackEvents_whenOnlyRecipientProvided_thenReturnFilteredResults() {
        String recipient = "João Silva";

        Pack pack1 = createPack(1L, "Loja ABC", recipient);
        Pack pack2 = createPack(2L, "Loja XYZ", recipient);

        List<Pack> packList = List.of(pack1, pack2);
        Page<Pack> page = new PageImpl<>(packList, pageable, packList.size());

        when(packRepository.findByRecipient(recipient, pageable)).thenReturn(page);

        try (var mockedConverter = mockStatic(PackConverter.class)) {
            mockedConverter.when(() -> PackConverter.toResponseDTO(any(Pack.class)))
                           .thenAnswer(invocation -> PackResponseDTO.builder()
                        		   .createdAt(LocalDateTime.now())
                        		   .deliveredAt(LocalDateTime.now())
                        		   .description("Livros para entrega")
                        		   .events(new ArrayList<PackEventDTO>())
                        		   .id(1L)
                        		   .recipient(recipient)
                        		   .sender(null)
                        		   .status(PackageStatus.CREATED)
                        		   .updatedAt(LocalDateTime.now())
                        		   .build());

            Page<PackResponseDTO> result = packHelperService.getPackEvents(null, recipient, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    @Test
    void testGetPackEvents_whenNoSenderOrRecipientProvided_thenReturnAllResults() {
        Pack pack1 = createPack(1L, "Loja ABC", "Cliente A");
        Pack pack2 = createPack(2L, "Loja XYZ", "Cliente B");

        List<Pack> packList = List.of(pack1, pack2);
        Page<Pack> page = new PageImpl<>(packList, pageable, packList.size());

        when(packRepository.findAll(pageable)).thenReturn(page);

        try (var mockedConverter = mockStatic(PackConverter.class)) {
            mockedConverter.when(() -> PackConverter.toResponseDTO(any(Pack.class)))
                           .thenAnswer(invocation -> PackResponseDTO.builder()
                        		   .createdAt(LocalDateTime.now())
                        		   .deliveredAt(LocalDateTime.now())
                        		   .description("Livros para entrega")
                        		   .events(new ArrayList<PackEventDTO>())
                        		   .id(1L)
                        		   .recipient(null)
                        		   .sender(null)
                        		   .status(PackageStatus.CREATED)
                        		   .updatedAt(LocalDateTime.now())
                        		   .build());

            Page<PackResponseDTO> result = packHelperService.getPackEvents(null, null, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    @Test
    void testGetPackEvents_whenNoResultsFound_thenReturnEmptyPage() {
        when(packRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<PackResponseDTO> result = packHelperService.getPackEvents(null, null, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}
