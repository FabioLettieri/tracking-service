package com.flsolution.mercadolivre.tracking_service.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flsolution.mercadolivre.tracking_service.dtos.request.HolidayRequest;
import com.flsolution.mercadolivre.tracking_service.services.impl.ExternalApiNagerServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiNagerService implements ExternalApiNagerServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExternalApiNagerService.class);
	private final RestTemplate restTemplate;

	@Value("${external-apis.nager-date-url}")
	private String nagerDateUrl;

	@Override
	@CircuitBreaker(name = "trackingService", fallbackMethod = "fallbackResponse")
	public Boolean isHoliday(String date) {
		logger.info("[START] - isHoliday() date: {}", date);

		try {
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int year = localDate.getYear();
            String url = String.format("%s/%d/%s", nagerDateUrl, year, "BR");

            HolidayRequest[] holidays = restTemplate.getForObject(url, HolidayRequest[].class);
            List<HolidayRequest> holidayList = Arrays.asList(holidays);

            boolean isHoliday = holidayList.stream()
                    .anyMatch(holiday -> LocalDate.parse(holiday.date()).equals(localDate));

            logger.info("[FINISH] - isHoliday() result: {}", isHoliday);
            return isHoliday;
        } catch (Exception ex) {
            logger.error("[ERROR] - isHoliday() WITH ERRORS: {}", ex.getMessage());
            return false;
        }
	}
	
	public ResponseEntity<String> fallbackResponse(String url, Throwable t) {
		logger.error("[START] - fallbackResponse()");
		ResponseEntity<String> response = ResponseEntity.status(503).body("Serviço ExternalApiNagerService temporariamente indisponível.");
		
		logger.error("[FINISH] - fallbackResponse() WITH ERRORS: {}", response.getBody());
        return response;
    }

}
