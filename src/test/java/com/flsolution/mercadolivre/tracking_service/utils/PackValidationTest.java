package com.flsolution.mercadolivre.tracking_service.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

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
}
