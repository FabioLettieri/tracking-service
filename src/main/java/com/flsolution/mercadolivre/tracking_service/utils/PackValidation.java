package com.flsolution.mercadolivre.tracking_service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackValidation {
	
	private static final Logger logger = LoggerFactory.getLogger(PackValidation.class);
	
	public static boolean validateStatusTransition(PackageStatus currentStatus, PackageStatus packageStatus) {
		logger.info("[START] - isValidTransition() currentStatus: {}, packageStatus: {}", currentStatus, packageStatus);

		if (currentStatus == PackageStatus.CREATED && packageStatus == PackageStatus.IN_TRANSIT) {
			logger.info("[FINISH] - isValidTransition() - TRUE");
			return true;
		}
		if (currentStatus == PackageStatus.IN_TRANSIT && packageStatus == PackageStatus.DELIVERED) {
			logger.info("[FINISH] - isValidTransition() - TRUE");
			return true;
		}
		
		logger.info("[FINISH] - isValidTransition() - FALSE");
		
		return false;
	}

}
