package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.dtos.HolidayResponseDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.ExternalApiNagerServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiNagerService implements ExternalApiNagerServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExternalApiNagerService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${external-apis.nager-date-url}")
    private String nagerDateUrl;

    public Boolean isHolliday(String date) {
        logger.info("[START] - isHolliday() date: {}", date);

        try {
            String url = String.format("%s/%s", nagerDateUrl, date);
            String response = restTemplate.getForObject(url, String.class);

            HolidayResponseDTO holidayResponse = objectMapper.readValue(response, HolidayResponseDTO.class);
            boolean isHoliday = holidayResponse.isHoliday();

            logger.info("[FINISH] - isHolliday() result: {}", isHoliday);
            return isHoliday;
        } catch (Exception ex) {
            logger.error("[ERROR] - isHolliday() WITH ERRORS: {}", ex.getMessage());
            return false;
        }
    }

    

}
