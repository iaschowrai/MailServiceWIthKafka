package com.iaschowrai.kafka_mailservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "email.provider")
@Getter
@Setter
public class EmailProviderConfig {
    private Map<String, String> properties;

    private Auth auth;
    @Getter
    @Setter
    public static class Auth{
        private String username;
        private String password;

    }
}
