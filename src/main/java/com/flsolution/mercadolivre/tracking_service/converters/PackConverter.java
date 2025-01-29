package com.flsolution.mercadolivre.tracking_service.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PackConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(PackConverter.class);

	public static Pack toEntity(PackRequestDTO request) {
		logger.info("[START] - toEntity() request: {}", request);
		Pack response = Pack.builder()
				.description(request.getDescription())
				.sender(request.getSender())
				.recipient(request.getRecipient())
				.isHolliday(request.getIsHolliday())
				.funFact(request.getFunfact())
				.estimatedDeliveryDate(request.getEstimatedDeliveryDate())
				.status(PackageStatus.CREATED)
				.build();
		
		logger.info("[FINISH] - toEntity()");

		return response;
	}
	
	public static PackResponseDTO toResponseDTO(Pack pack) {
		logger.info("[START] - toResponseDTO() pack: {}", pack);
		PackResponseDTO response = new PackResponseDTO(
				pack.getId(),
				pack.getDescription(),
				pack.getSender(),
				pack.getRecipient(),
				pack.getStatus(),
				pack.getCreatedAt(),
				pack.getUpdatedAt());
		
		if (pack.getDeliveredAt() != null) {
	        response.setDeliveredAt(pack.getDeliveredAt());
	    }
		
		
		logger.info("[FINISH] - toResponseDTO()");

		return response;
	}

}
