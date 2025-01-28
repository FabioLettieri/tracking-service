package com.flsolution.mercadolivre.tracking_service.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityBaseTest {

    private EntityBase entityBase;

    @SuppressWarnings("serial")
	@BeforeEach
    void setUp() {
        entityBase = new EntityBase() {};
    }

    @Test
    void testOnCreate() throws Exception {
        Method onCreateMethod = EntityBase.class.getDeclaredMethod("onCreate");
        onCreateMethod.setAccessible(true);

        onCreateMethod.invoke(entityBase);

        assertNotNull(entityBase.getCreatedAt(), "CreatedAt should not be null after onCreate()");
        assertNotNull(entityBase.getUpdatedAt(), "UpdatedAt should not be null after onCreate()");
        assertTrue(entityBase.getCreatedAt().isBefore(LocalDateTime.now()) || entityBase.getCreatedAt().isEqual(LocalDateTime.now()));
        assertTrue(entityBase.getUpdatedAt().isBefore(LocalDateTime.now()) || entityBase.getUpdatedAt().isEqual(LocalDateTime.now()));
    }

    @Test
    void testOnUpdate() throws Exception {
        Method onCreateMethod = EntityBase.class.getDeclaredMethod("onCreate");
        Method onUpdateMethod = EntityBase.class.getDeclaredMethod("onUpdate");

        onCreateMethod.setAccessible(true);
        onUpdateMethod.setAccessible(true);

        onCreateMethod.invoke(entityBase);
        LocalDateTime createdAtBeforeUpdate = entityBase.getCreatedAt();
        LocalDateTime updatedAtBeforeUpdate = entityBase.getUpdatedAt();

        Thread.sleep(10);

        onUpdateMethod.invoke(entityBase);

        assertEquals(createdAtBeforeUpdate, entityBase.getCreatedAt(), "CreatedAt should not change on update");
        assertNotNull(entityBase.getUpdatedAt(), "UpdatedAt should not be null after onUpdate()");
        assertTrue(entityBase.getUpdatedAt().isAfter(updatedAtBeforeUpdate), "UpdatedAt should be updated to a later time");
    }
}
