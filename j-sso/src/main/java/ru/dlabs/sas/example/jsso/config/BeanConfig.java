package ru.dlabs.sas.example.jsso.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.components.RegistrationStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisOTPStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisRegistrationStore;
import ru.dlabs71.library.email.DEmailSender;
import ru.dlabs71.library.email.property.SmtpProperties;

@Configuration
public class BeanConfig {

    @Bean
    @ConfigurationProperties(prefix = "d-email")
    public SmtpProperties smtpProperties() {
        return new SmtpProperties();
    }

    @Bean
    public DEmailSender emailSender(SmtpProperties smtpProperties) {
        return DEmailSender.of(smtpProperties);
    }

    @Bean
    public OTPStore otpStore(StringRedisTemplate redisTemplate) {
        return new RedisOTPStore(redisTemplate);
    }

    @Bean
    public RegistrationStore registrationStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new RedisRegistrationStore(redisTemplate, objectMapper);
    }
}
