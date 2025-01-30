package com.flsolution.mercadolivre.tracking_service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusCanceledException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusDeliveredException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CancelPackStatusInTransitException;

class PackValidationTest {

    @Test
    void testValidateStatusTransition_whenCreatedToInTransit_thenReturnTrue() {
        boolean result = PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.IN_TRANSIT);
        assertTrue(result, "A transição de CREATED para IN_TRANSIT deve ser válida.");
    }

    @Test
    void testValidateStatusTransition_whenInTransitToDelivered_thenReturnTrue() {
        boolean result = PackValidation.validateStatusTransition(PackageStatus.IN_TRANSIT, PackageStatus.DELIVERED);
        assertTrue(result, "A transição de IN_TRANSIT para DELIVERED deve ser válida.");
    }

    @Test
    void testValidateStatusTransition_whenInvalidTransition_thenReturnFalse() {
        boolean result = PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.DELIVERED);
        assertFalse(result, "A transição direta de CREATED para DELIVERED deve ser inválida.");
    }

    @Test
    void testValidateStatusTransition_whenSameStatus_thenReturnFalse() {
        boolean result = PackValidation.validateStatusTransition(PackageStatus.CREATED, PackageStatus.CREATED);
        assertFalse(result, "A transição de um status para ele mesmo deve ser inválida.");
    }

    @Test
    void testValidateStatusTransition_whenDeliveredToInTransit_thenReturnFalse() {
        boolean result = PackValidation.validateStatusTransition(PackageStatus.DELIVERED, PackageStatus.IN_TRANSIT);
        assertFalse(result, "A transição de DELIVERED para IN_TRANSIT deve ser inválida.");
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
}
