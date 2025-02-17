package com.flsolution.mercadolivre.tracking_service.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusCanceledException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusDeliveredException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusInTransitException;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackCreateDuplicateDetected;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackStatusInvalidException;

public class PackValidation {

	private static final String FINISH_WITH_ERRORS_STATUS_TRANSITION = "[FINISH] - validateStatusTransition() WITH ERRORS";
	private static final String FINISH_WITH_ERRORS_ELEGIBLE_FOR_CANCELLATION = "[FINISH] - validatePackElegibleForCancellation() WITH ERRORS";
	
	private static final Logger logger = LoggerFactory.getLogger(PackValidation.class);
	
	private static final Map<PackageStatus, Set<PackageStatus>> validTransitions = new HashMap<PackageStatus, Set<PackageStatus>>();
	private static final EnumSet<PackageStatus> invalidTransitions = EnumSet.of(PackageStatus.IN_TRANSIT,
			PackageStatus.CANCELLED, PackageStatus.CANCELED_DUE_TO_INACTIVITY, PackageStatus.DELIVERED);

	static {
		validTransitions.put(PackageStatus.CREATED, Set.of(PackageStatus.IN_TRANSIT));
		validTransitions.put(PackageStatus.IN_TRANSIT, Set.of(PackageStatus.DELIVERED));
		validTransitions.put(PackageStatus.DELIVERED, Set.of());
	}

	public static void validateStatusTransition(PackageStatus currentStatus, PackageStatus packageStatus) {
		logger.info("[START] - validateStatusTransition() currentStatus: {}, packageStatus: {}", currentStatus, packageStatus);

	    if (currentStatus == packageStatus) {
	        logger.info(FINISH_WITH_ERRORS_STATUS_TRANSITION);
	        throw new PackStatusInvalidException("Transitions are not allowed when the status current and update are the same.");
	    }

	    var validNextStatus = validTransitions.getOrDefault(currentStatus, Set.of());
	    if (!validNextStatus.contains(packageStatus)) {
	        logger.info(FINISH_WITH_ERRORS_STATUS_TRANSITION);
	        throw new PackStatusInvalidException(String.format("Invalid transition from %s to %s", currentStatus, packageStatus));
	    }

	    logger.info("[FINISH] - validateStatusTransition()");
	}

	public static void validatePackElegibleForCancellation(PackageStatus status) throws BadRequestException {
        logger.info("[START] - validatePackElegibleForCancellation() status: {}", status);

        if (status == PackageStatus.IN_TRANSIT) {
            logger.info(FINISH_WITH_ERRORS_ELEGIBLE_FOR_CANCELLATION);
            throw new CancelPackStatusInTransitException("The package is already on its way and cannot be canceled. Wait for delivery and refuse.");
        }

        if (invalidTransitions.contains(status)) {
            if (status == PackageStatus.IN_TRANSIT) {
                logger.info(FINISH_WITH_ERRORS_ELEGIBLE_FOR_CANCELLATION);
                throw new CancelPackStatusInTransitException("The package is already on its way and cannot be canceled. Wait for delivery and refuse.");
            } else if (status == PackageStatus.CANCELLED || status == PackageStatus.CANCELED_DUE_TO_INACTIVITY) {
                logger.info(FINISH_WITH_ERRORS_ELEGIBLE_FOR_CANCELLATION);
                throw new CancelPackStatusCanceledException("Package is already canceled, no action will be registered.");
            } else if (status == PackageStatus.DELIVERED) {
                logger.info(FINISH_WITH_ERRORS_ELEGIBLE_FOR_CANCELLATION);
                throw new CancelPackStatusDeliveredException("Action not carried out, since it has already been delivered.");
            }
        }

        logger.info("[FINISH] - validatePackElegibleForCancellation()");
	}
	
	public static void validateDuplicateRequest(Optional<List<Pack>> optPack) throws PackCreateDuplicateDetected {
		logger.info("[START] - validateDuplicateRequest() optPack: {}", optPack);
		if (optPack.isPresent()) {
			List<Pack> listPack = optPack.get();
			Pack pack = listPack.getLast();
			
			LocalDateTime createdAt = pack.getCreatedAt();
	        LocalDateTime now = LocalDateTime.now();
	        
	        if (Duration.between(createdAt, now).toMinutes() <= 2) {
	        	logger.error("[FINISH] - validateDuplicateRequest() WITH ERRORS");
	        	throw new PackCreateDuplicateDetected("Duplicate orders detected, as there is an order placed less than 2 minutes ago.");
	        }
	    }
		logger.info("[FINISH] - validateDuplicateRequest()");
	}
}
