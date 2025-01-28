package com.flsolution.mercadolivre.tracking_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

}
