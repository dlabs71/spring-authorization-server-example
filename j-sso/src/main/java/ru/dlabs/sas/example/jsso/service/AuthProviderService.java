package ru.dlabs.sas.example.jsso.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface AuthProviderService {

    /**
     * Создание или обновление пользователя используя сервис-провайдер
     */
    UserEntity save(OAuth2User userDto, AuthProvider provider);

    /**
     * Создание или обновление пользователя с последующим маппингом в сущность AuthorizedUser
     */
    AuthorizedUser saveAndMap(OAuth2User userDto, AuthProvider provider);
}
