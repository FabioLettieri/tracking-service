package com.flsolution.mercadolivre.tracking_service.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flsolution.mercadolivre.tracking_service.dtos.HolidayRequestDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.ExternalApiNagerServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiNagerService implements ExternalApiNagerServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExternalApiNagerService.class);
	private final RestTemplate restTemplate;

	@Value("${external-apis.nager-date-url}")
	private String nagerDateUrl;

	public Boolean isHoliday(String date) {
		logger.info("[START] - isHoliday() date: {}", date);

		try {
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int year = localDate.getYear();
            String url = String.format("%s/%d/%s", nagerDateUrl, year, "BR");

            HolidayRequestDTO[] holidays = restTemplate.getForObject(url, HolidayRequestDTO[].class);
            List<HolidayRequestDTO> holidayList = Arrays.asList(holidays);

            boolean isHoliday = holidayList.stream()
                    .anyMatch(holiday -> LocalDate.parse(holiday.getDate()).equals(localDate));

            logger.info("[FINISH] - isHoliday() result: {}", isHoliday);
            return isHoliday;
        } catch (Exception ex) {
            logger.error("[ERROR] - isHoliday() WITH ERRORS: {}", ex.getMessage());
            return false;
        }
	}

}
