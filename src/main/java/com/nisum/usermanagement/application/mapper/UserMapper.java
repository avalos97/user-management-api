package com.nisum.usermanagement.application.mapper;

import com.nisum.usermanagement.application.dto.PhoneRequest;
import com.nisum.usermanagement.application.dto.PhoneResponse;
import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.domain.model.aggregate.Phone;
import com.nisum.usermanagement.domain.model.aggregate.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de la capa de aplicación 
 */
public class UserMapper {

    private UserMapper() {
    }

    public static List<Phone> toPhoneDomain(List<PhoneRequest> phoneRequests) {
        return phoneRequests.stream()
                .map(pr -> Phone.create(pr.getNumber(), pr.getCitycode(), pr.getCountrycode()))
                .collect(Collectors.toList());
    }

    public static List<PhoneResponse> toPhoneResponses(List<Phone> phones) {
        return phones.stream()
                .map(p -> new PhoneResponse(p.getNumber(), p.getCitycode(), p.getCountrycode()))
                .collect(Collectors.toList());
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken().getValue(),
                user.isActive(),
                user.getName(),
                user.getEmail().getValue(),
                toPhoneResponses(user.getPhones())
        );
    }
}
