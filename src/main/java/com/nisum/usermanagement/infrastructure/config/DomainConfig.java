package com.nisum.usermanagement.infrastructure.config;

import com.nisum.usermanagement.domain.service.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class DomainConfig {
    private final AppProperties appProperties;

    @Bean
    public Pattern emailPattern() {
        return Pattern.compile(appProperties.getEmailRegex());
    }

    @Bean
    public PasswordPolicy passwordPolicy() {
        Pattern passwordPattern = Pattern.compile(appProperties.getPasswordRegex());
        return new PasswordPolicy(passwordPattern);
    }
}
