package com.flsolution.mercadolivre.tracking_service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusCanceledException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusDeliveredException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusInTransitException;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackCreateDuplicateDetectedException;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackStatusInvalidException;

class PackValidationTest {

	@Test
    void testValidateStatusTransition_whenCreatedToInTransit_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.IN_TRANSIT),
            "A transição de CREATED para IN_TRANSIT deve ser válida.");
    }

    @Test
    void testValidateStatusTransition_whenInTransitToDelivered_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> PackValidation.validateStatusTransition(PackageStatus.IN_TRANSIT, PackageStatus.DELIVERED),
            "A transição de IN_TRANSIT para DELIVERED deve ser válida.");
    }

    @Test
    void testValidateStatusTransition_whenInvalidTransition_thenThrowsException() {
        assertThrows(PackStatusInvalidException.class,
            () -> PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.DELIVERED),
            "A transição direta de CREATED para DELIVERED deve ser inválida.");
    }

    @Test
    void testValidateStatusTransition_whenSameStatus_thenThrowsException() {
        assertThrows(PackStatusInvalidException.class,
            () -> PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.CREATED),
            "A transição de um status para ele mesmo deve ser inválida.");
    }

    @Test
    void testValidateStatusTransition_whenDeliveredToInTransit_thenThrowsException() {
        assertThrows(PackStatusInvalidException.class,
            () -> PackValidation.validateStatusTransition(PackageStatus.DELIVERED, PackageStatus.IN_TRANSIT),
            "A transição de DELIVERED para IN_TRANSIT deve ser inválida.");
    }
    
    @Test
    void testValidatePackElegibleForCancellation_whenStatusInTransit_thenThrowCancelPackStatusInTransitException() {
        Exception exception = assertThrows(CancelPackStatusInTransitException.class, 
            () -> PackValidation.validatePackElegibleForCancellation(PackageStatus.IN_TRANSIT));

        assertEquals("The package is already on its way and cannot be canceled. Wait for delivery and refuse.", exception.getMessage());
    }

    @Test
    void testValidatePackElegibleForCancellation_whenStatusCancelled_thenThrowCancelPackStatusCanceledException() {
        Exception exception = assertThrows(CancelPackStatusCanceledException.class, 
            () -> PackValidation.validatePackElegibleForCancellation(PackageStatus.CANCELLED));

        assertEquals("Package is already canceled, no action will be registered.", exception.getMessage());
    }

    @Test
    void testValidatePackElegibleForCancellation_whenStatusCanceledDueToInactivity_thenThrowCancelPackStatusCanceledException() {
        Exception exception = assertThrows(CancelPackStatusCanceledException.class, 
            () -> PackValidation.validatePackElegibleForCancellation(PackageStatus.CANCELED_DUE_TO_INACTIVITY));

        assertEquals("Package is already canceled, no action will be registered.", exception.getMessage());
    }

    @Test
    void testValidatePackElegibleForCancellation_whenStatusDelivered_thenThrowCancelPackStatusDeliveredException() {
        Exception exception = assertThrows(CancelPackStatusDeliveredException.class, 
            () -> PackValidation.validatePackElegibleForCancellation(PackageStatus.DELIVERED));

        assertEquals("Action not carried out, since it has already been delivered.", exception.getMessage());
    }

    @Test
    void testValidatePackElegibleForCancellation_whenStatusCreated_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> PackValidation.validatePackElegibleForCancellation(PackageStatus.CREATED));
    }
    
    @Test
    void testValidateDuplicateRequest_noDuplicate() {
    	Pack pack = new Pack();
    	pack.setCreatedAt(LocalDateTime.now().minusMinutes(3));
        List<Pack> packList = new ArrayList<>();
        packList.add(pack);
        Optional<List<Pack>> optPack = Optional.of(packList);

        assertDoesNotThrow(() -> PackValidation.validateDuplicateRequest(optPack));
    }

    @Test
    void testValidateDuplicateRequest_duplicateDetected() {
    	Pack pack = new Pack();
    	pack.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        List<Pack> packList = new ArrayList<>();
        packList.add(pack);
        Optional<List<Pack>> optPack = Optional.of(packList);

        assertThrows(PackCreateDuplicateDetectedException.class, () -> PackValidation.validateDuplicateRequest(optPack),
            "Duplicate orders detected, as there is an order placed less than 2 minutes ago.");
    }

    @Test
    void testValidateDuplicateRequest_emptyOptional() {
        Optional<List<Pack>> optPack = Optional.empty();

        assertDoesNotThrow(() -> PackValidation.validateDuplicateRequest(optPack));
    }
}
