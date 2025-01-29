package com.flsolution.mercadolivre.tracking_service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayResponseDTO {
	
	@JsonProperty("holiday")
	private boolean holiday;
	
	public boolean isHoliday() {
	    return holiday;
	}

}
