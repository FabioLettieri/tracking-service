package com.flsolution.mercadolivre.tracking_service.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.dtos.request.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.CustomerServiceImpl;
import com.flsolution.mercadolivre.tracking_service.utils.CacheControlUtils;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerServiceImpl customerServiceImpl;

    @Mock
    private ETagService eTagService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateCustomerSuccessfully() throws Exception {
        CustomerRequest requestDTO = new CustomerRequest("Rua Exemplo", "12345678900", "email@email.com", "Fulano de Tal", "999999999");

        CustomerResponse responseDTO = CustomerResponse.builder()
                .id(1L)
                .name("Fulano de Tal")
                .email("email@email.com")
                .document("12345678900")
                .address("Rua Exemplo")
                .phoneNumber("999999999")
                .build();

        when(customerServiceImpl.createCustomer(any(CustomerRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isOk());
    }


    @Test
    void testGetCustomers_cacheNotModified() throws Exception {
        Page<CustomerResponse> responsePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 50), 0);

        when(customerServiceImpl.getCustomers(any(), any(), any(PageRequest.class)))
                .thenReturn(responsePage);
        when(eTagService.generateETag(any())).thenReturn("etag");
        when(eTagService.isNotModified(any(), any())).thenReturn(true);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isNotModified());
    }
    
    @Test
    void testGetCustomers_cacheModified() throws Exception {
        Page<CustomerResponse> responsePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 50), 0);

        when(customerServiceImpl.getCustomers(any(), any(), any(PageRequest.class)))
                .thenReturn(responsePage);
        when(eTagService.generateETag(any())).thenReturn("etag");
        when(eTagService.isNotModified(any(), any())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andReturn();

        String eTag = mvcResult.getResponse().getHeader(HttpHeaders.ETAG);
        String cacheControl = mvcResult.getResponse().getHeader(HttpHeaders.CACHE_CONTROL);

        assertEquals("etag", eTag.replace("\"", ""));
        assertEquals(CacheControlUtils.getCacheControl().getHeaderValue(), cacheControl);
    }
    
    
}
