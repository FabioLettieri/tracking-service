package com.flsolution.mercadolivre.tracking_service.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;

@ExtendWith(MockitoExtension.class)
class PackBatchProcessorTest {

    @InjectMocks
    private PackBatchProcessor packBatchProcessor;

    @Mock
    private PackRepository packRepository;

    @Test
    void testDeactivateOldInTransitPackages() {
        when(packRepository.deactivateOldPackagesWithStatusInTransit(
                any(LocalDateTime.class),
                eq(PackageStatus.IN_TRANSIT.toString()),
                eq(PackageStatus.CANCELED_DUE_TO_INACTIVITY.toString())
        )).thenReturn(5);

        packBatchProcessor.deactivateOldInTransitPackages();

        verify(packRepository, times(1)).deactivateOldPackagesWithStatusInTransit(
                any(LocalDateTime.class),
                eq(PackageStatus.IN_TRANSIT.toString()),
                eq(PackageStatus.CANCELED_DUE_TO_INACTIVITY.toString())
        );
    }
}
