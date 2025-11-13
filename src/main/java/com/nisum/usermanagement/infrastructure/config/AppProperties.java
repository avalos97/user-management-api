package com.nisum.usermanagement.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String emailRegex;
    
    private String passwordRegex;
    
    private JwtProperties jwt;
}
