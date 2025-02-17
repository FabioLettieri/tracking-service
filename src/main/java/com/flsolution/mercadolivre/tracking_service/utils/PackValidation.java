package com.flsolution.mercadolivre.tracking_service.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusCanceledException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusDeliveredException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusInTransitException;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackStatusInvalidException;

public class PackValidation {

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
		logger.info("[START] - validateStatusTransition() currentStatus: {}, packageStatus: {}", currentStatus,
				packageStatus);

		if (currentStatus == packageStatus) {
			logger.info("[FINISH] - validateStatusTransition() WITH ERRORS");
			throw new PackStatusInvalidException(
					"Transitions are not allowed when the status current and update are the same.");
		}

		var validNextStatus = validTransitions.getOrDefault(packageStatus, Set.of());
		if (!validNextStatus.contains(packageStatus)) {
			logger.info("[FINISH] - validateStatusTransition() WITH ERRORS");
			throw new PackStatusInvalidException(
					String.format("Invalid transition from %s to %s", currentStatus, packageStatus));
		}

		logger.info("[FINISH] - validateStatusTransition()");
	}

	public static void validatePackElegibleForCancellation(PackageStatus status) throws BadRequestException {
        logger.info("[START] - validatePackElegibleForCancellation() status: {}", status);

        if (status == PackageStatus.IN_TRANSIT) {
            logger.info("[FINISH] - validatePackElegibleForCancellation() WITH ERRORS");
            throw new CancelPackStatusInTransitException("The package is already on its way and cannot be canceled. Wait for delivery and refuse.");
        }

        if (invalidTransitions.contains(status)) {
            if (status == PackageStatus.IN_TRANSIT) {
                logger.info("[FINISH] - validatePackElegibleForCancellation() WITH ERRORS");
                throw new CancelPackStatusInTransitException("The package is already on its way and cannot be canceled. Wait for delivery and refuse.");
            } else if (status == PackageStatus.CANCELLED || status == PackageStatus.CANCELED_DUE_TO_INACTIVITY) {
                logger.info("[FINISH] - validatePackElegibleForCancellation() WITH ERRORS");
                throw new CancelPackStatusCanceledException("Package is already canceled, no action will be registered.");
            } else if (status == PackageStatus.DELIVERED) {
                logger.info("[FINISH] - validatePackElegibleForCancellation() WITH ERRORS");
                throw new CancelPackStatusDeliveredException("Action not carried out, since it has already been delivered.");
            }
        }

        logger.info("[FINISH] - validatePackElegibleForCancellation()");
	}
}
