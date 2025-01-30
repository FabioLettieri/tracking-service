package com.flsolution.mercadolivre.tracking_service.controllers;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zaxxer.hikari.HikariDataSource;

@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/connections")
    public String getConnections() throws SQLException {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        return "Conexões Ativas: " + hikariDataSource.getHikariPoolMXBean().getActiveConnections();
    }
}
