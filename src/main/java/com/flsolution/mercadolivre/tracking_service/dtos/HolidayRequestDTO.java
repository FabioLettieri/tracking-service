package com.flsolution.mercadolivre.tracking_service.dtos;

import lombok.Data;

@Data
public class HolidayRequestDTO {
    private String date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
    private String type;
}