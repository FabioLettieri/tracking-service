package com.flsolution.mercadolivre.tracking_service.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.updates.UpdateStatusRequest;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.PackEventService;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;

@ExtendWith(MockitoExtension.class)
class PackControllerTest {

	@Mock
	private PackServiceImpl packService;

	@Mock
	private PackEventService packEventService;

	@InjectMocks
	private PackController packController;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Mock
	private ETagService eTagService;
	
	@Mock
	private CacheControl cacheControlMock;	

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(packController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()) // Adiciona suporte a Pageable
				.build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void testCreatePackSuccessfully() throws Exception {
		PackRequestDTO requestDTO = new PackRequestDTO("Livros para entrega", "Loja ABC", "João Silva", true,
				"24/10/2025", 1L);

		PackResponseDTO responseDTO = PackResponseDTO.builder()
	     		   .createdAt(LocalDateTime.now())
	     		   .deliveredAt(LocalDateTime.now())
	     		   .description("Livros para entrega")
	     		   .events(new ArrayList<PackEventDTO>())
	     		   .id(1L)
	     		   .recipient(null)
	     		   .sender(null)
	     		   .status(PackageStatus.CREATED)
	     		   .updatedAt(LocalDateTime.now())
  		   .build();

		lenient().when(packService.createPack(any(PackRequestDTO.class))).thenReturn(responseDTO);

		mockMvc.perform(post("/api/v1/packs").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isOk());
	}

	@Test
	void testCreatePackWithValidRequest() throws Exception {
		PackRequestDTO requestDTO = new PackRequestDTO("Livros para entrega", "Loja ABC", "João Silva", true,
				"24/10/2025", 1L);

		mockMvc.perform(post("/api/v1/packs").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isOk());
	}

	@Test
	void testCreatePackWithInvalidRequest() throws Exception {
		PackRequestDTO invalidRequest = null;

		mockMvc.perform(post("/api/v1/packs").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateStatusPackSuccessfully() throws Exception {
		Long packId = 1L;
		UpdateStatusRequest updateStatusRequest = UpdateStatusRequest.builder()
				.status(PackageStatus.IN_TRANSIT)
				.build();

		PackResponseDTO responseDTO = PackResponseDTO.builder()
	     		   .createdAt(LocalDateTime.now())
	     		   .deliveredAt(LocalDateTime.now())
	     		   .description("Livros para entrega")
	     		   .events(new ArrayList<PackEventDTO>())
	     		   .id(1L)
	     		   .recipient(null)
	     		   .sender(null)
	     		   .status(PackageStatus.CREATED)
	     		   .updatedAt(LocalDateTime.now())
  		   .build();

		lenient().when(packService.updateStatusPack(eq(packId), eq(PackageStatus.IN_TRANSIT))).thenReturn(responseDTO);

		mockMvc.perform(put("/api/v1/packs/{id}/status", packId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateStatusRequest))).andExpect(status().isOk());
	}

	@Test
	void testUpdateStatusPackWithInvalidTransition() throws Exception {
		Long packId = 1L;

		PackResponseDTO responseDTO = PackResponseDTO.builder()
	     		   .createdAt(LocalDateTime.now())
	     		   .deliveredAt(LocalDateTime.now())
	     		   .description("Livros para entrega")
	     		   .events(new ArrayList<PackEventDTO>())
	     		   .id(1L)
	     		   .recipient(null)
	     		   .sender(null)
	     		   .status(PackageStatus.CREATED)
	     		   .updatedAt(LocalDateTime.now())
  		   .build();

		lenient().when(packService.updateStatusPack(eq(packId), eq(PackageStatus.DELIVERED))).thenReturn(responseDTO);

		mockMvc.perform(put("/api/v1/packs/{id}/status", packId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PackageStatus.DELIVERED))).andExpect(status().isBadRequest());
	}
	
    @Test
    void testGetPackById_whenInvalidId_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/packs/{id}", "invalid-id"))
                .andExpect(status().isBadRequest());
    }
}
 