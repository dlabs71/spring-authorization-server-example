package ru.dlabs.sas.example.jservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppProperties {

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "spring.mvc.cors")
    public static class CorsProperties {

        private List<CorsConfig> configs;

        public static record CorsConfig(
                String pattern,
                String allowedOrigins,
                String allowedOriginPatterns,
                String allowedHeaders,
                String exposedHeaders,
                String allowedMethods,
                Boolean allowCredentials,
                Long maxAge
        ) {

        }
    }
}