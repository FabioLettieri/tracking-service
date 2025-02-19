package com.flsolution.mercadolivre.tracking_service.controllers;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    private final DataSource dataSource;

	@GetMapping("/connections")
    public String getConnections() throws SQLException {
		try {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            return "Conexões Ativas: " + hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        } catch (Exception e) {
            throw new SQLException("Database error", e);
        }
    }
}
