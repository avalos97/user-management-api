package com.nisum.usermanagement.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Phone {
    private UUID id;
    private String number;
    private String citycode;
    private String countrycode;

    public static Phone create(String number, String citycode, String countrycode) {
        return new Phone(UUID.randomUUID(), number, citycode, countrycode);
    }
}
