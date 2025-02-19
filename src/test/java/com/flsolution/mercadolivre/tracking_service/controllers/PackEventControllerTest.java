package com.flsolution.mercadolivre.tracking_service.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flsolution.mercadolivre.tracking_service.dtos.request.PackEventRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventProducerServiceImpl;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventServiceImpl;

@ExtendWith(MockitoExtension.class)
class PackEventControllerTest {

    @Mock
    private PackEventServiceImpl packEventServiceImpl;

    @Mock
    private PackEventProducerServiceImpl packEventProducerServiceImpl;

    @Mock
    private ETagService eTagService;
    
    @InjectMocks
    private PackEventController packEventController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(packEventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testGetPackEvents_cacheNotModified() throws Exception {
        Page<PackEventResponse> responsePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 50), 0);

        when(packEventServiceImpl.getPackEvents(any(PageRequest.class)))
                .thenReturn(responsePage);
        when(eTagService.generateETag(any())).thenReturn("etag");
        when(eTagService.isNotModified(any(), any())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/pack-events"))
                .andExpect(status().isNotModified())
                .andReturn();

        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGetPackEvents_cacheModified() throws Exception {
        Page<PackEventResponse> responsePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 50), 0);
        CacheControl cacheControl = CacheControl.maxAge(5, java.util.concurrent.TimeUnit.MINUTES);

        when(packEventServiceImpl.getPackEvents(any(PageRequest.class)))
                .thenReturn(responsePage);
        when(packEventServiceImpl.getCacheControl()).thenReturn(cacheControl);
        when(eTagService.generateETag(any())).thenReturn("etag");
        when(eTagService.isNotModified(any(), any())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/pack-events"))
                .andExpect(status().isOk())
                .andReturn();

        String eTag = mvcResult.getResponse().getHeader(HttpHeaders.ETAG);
        String cacheControlHeader = mvcResult.getResponse().getHeader(HttpHeaders.CACHE_CONTROL);

        assertEquals("etag", eTag.replace("\"", ""));
        assertEquals(cacheControl.getHeaderValue(), cacheControlHeader);
    }

    @Test
    void testCreatePackEventSuccessfully() throws Exception {
        PackEventRequest requestDTO = PackEventRequest.builder()
        		.description("Evento de Teste")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location teste")
        		.packId(1L)
        		.build();

        mockMvc.perform(post("/api/v1/pack-events").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isCreated());
    }

    @Test
    void testCreatePackEventWithInvalidRequest() throws Exception {
        PackEventRequest invalidRequest = PackEventRequest.builder()
        		.description(null)
        		.eventDateTime(LocalDateTime.now())
        		.location(null)
        		.packId(null)
        		.build();

        mockMvc.perform(post("/api/v1/pack-events").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateListPackEventSuccessfully() throws Exception {
        List<PackEventRequest> requestDTOList = new ArrayList<>();
        requestDTOList.add(PackEventRequest.builder()
        		.description("Evento de Teste")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location teste")
        		.packId(1L)
        		.build());
        
        requestDTOList.add(
        		PackEventRequest.builder()
        		.description("Evento de Teste 2")
        		.eventDateTime(LocalDateTime.now())
        		.location("Location teste 2")
        		.packId(2L)
        		.build());

        mockMvc.perform(post("/api/v1/pack-events/list").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTOList))).andExpect(status().isCreated());
    }

    @Test
    void testCreateListPackEventWithInvalidRequest() throws Exception {
        List<PackEventRequest> invalidRequestList = new ArrayList<>();
        invalidRequestList.add(new PackEventRequest(null, null, null, null));

        mockMvc.perform(post("/api/v1/pack-events/list").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestList))).andExpect(status().isBadRequest());
    }
}
