package com.nisum.usermanagement.application.dto;

public record PhoneResponse(
        String number,
        String citycode,
        String countrycode
) {}
