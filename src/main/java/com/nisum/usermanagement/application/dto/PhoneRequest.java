package com.nisum.usermanagement.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El número debe contener entre 6 y 15 dígitos")
    private String number;

    @NotBlank(message = "El código de ciudad es obligatorio")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "El código de ciudad debe contener entre 1 y 5 dígitos")
    private String citycode;

    @NotBlank(message = "El código de país es obligatorio")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "El código de país debe contener entre 1 y 5 dígitos")
    private String countrycode;
}
