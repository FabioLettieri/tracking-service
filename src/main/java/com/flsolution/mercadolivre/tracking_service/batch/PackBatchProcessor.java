package com.flsolution.mercadolivre.tracking_service.batch;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PackBatchProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(PackEventBatchProcessor.class);
	private final PackRepository packRepository;
	
	@Async("taskExecutor")
	@Scheduled(cron = "0 0 0 * * ?")
	public void deactivateOldInTransitPackages() {
	    logger.info("[START] - deactivateOldInTransitPackages()");

	    LocalDateTime deactivateDate = LocalDateTime.now().minusDays(30);
	    Integer deletedCount = packRepository.deactivateOldPackagesWithStatusInTransit(deactivateDate, PackageStatus.IN_TRANSIT.toString(), PackageStatus.CANCELED_DUE_TO_INACTIVITY.toString());

	    logger.info("[FINISH] - deactivateOldInTransitPackages() deactivated registers: {}", deletedCount);
	}

}
