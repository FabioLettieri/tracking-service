package com.flsolution.mercadolivre.tracking_service.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {

    @Mock
    private HikariDataSource hikariDataSource;

    @Mock
    private HikariPoolMXBean hikariPoolMXBean;

    @InjectMocks
    private MonitoringController monitoringController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws SQLException {
        when(hikariDataSource.getHikariPoolMXBean()).thenReturn(hikariPoolMXBean);
        monitoringController = new MonitoringController(hikariDataSource);

        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build();
    }

    @Test
    void testGetConnections() throws Exception {
        when(hikariPoolMXBean.getActiveConnections()).thenReturn(5);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/monitoring/connections"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertEquals("Conexões Ativas: 5", responseContent);
    }

}
