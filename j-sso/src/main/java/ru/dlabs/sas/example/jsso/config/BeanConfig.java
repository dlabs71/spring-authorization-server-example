package ru.dlabs.sas.example.jsso.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import ru.dlabs.sas.example.jsso.components.ConfirmationStore;
import ru.dlabs.sas.example.jsso.components.FileStore;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.components.RegistrationStore;
import ru.dlabs.sas.example.jsso.components.impl.LocalFileStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisChangePasswordStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisOTPStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisRegistrationStore;
import ru.dlabs.sas.example.jsso.components.impl.RedisResetPasswordStore;
import ru.dlabs71.library.email.DEmailSender;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final AppProperties.EmailProperties emailProperties;
    private final AppProperties.FileStoreConfig fileStoreConfig;

    @Bean
    public OTPStore.Config otpStoreConfig(
        @Value("${otp-store.cookie-name:default-name}") String cookieName,
        @Value("${otp-store.cookie-domain:localhost}") String cookieDomain,
        @Value("${otp-store.cookie-max-age:180}") int cookieMaxAge
    ) {
        return new OTPStore.Config(cookieName, cookieDomain, cookieMaxAge);
    }

    @Bean
    public OTPStore otpStore(OTPStore.Config otpStoreConfig, StringRedisTemplate redisTemplate) {
        return new RedisOTPStore(otpStoreConfig, redisTemplate);
    }

    @Bean
    public RegistrationStore registrationStore(
        OTPStore.Config otpStoreConfig,
        StringRedisTemplate redisTemplate,
        ObjectMapper objectMapper
    ) {
        return new RedisRegistrationStore(otpStoreConfig.cookieMaxAge(), redisTemplate, objectMapper);
    }

    @Bean
    public ConfirmationStore resetPasswordStore(
        OTPStore.Config otpStoreConfig,
        StringRedisTemplate redisTemplate,
        ObjectMapper objectMapper
    ) {
        return new RedisResetPasswordStore(otpStoreConfig.cookieMaxAge(), redisTemplate, objectMapper);
    }

    @Bean
    public DEmailSender emailSender() {
        return DEmailSender.of(emailProperties);
    }

    @Bean
    public ConfirmationStore changePasswordStore(
        OTPStore.Config otpStoreConfig,
        StringRedisTemplate redisTemplate,
        ObjectMapper objectMapper
    ) {
        return new RedisChangePasswordStore(otpStoreConfig.cookieMaxAge(), redisTemplate, objectMapper);
    }

    @Bean
    public FileStore fileStore() {
        return new LocalFileStore(Path.of(fileStoreConfig.getBasePath()));
    }
}
