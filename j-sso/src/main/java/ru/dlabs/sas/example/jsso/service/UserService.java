package ru.dlabs.sas.example.jsso.service;

import java.util.UUID;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

public interface UserService {

    /**
     * Создание или обновление пользователя используя сервис-провайдер
     */
    UserEntity save(OAuth2User userDto, AuthProvider provider);

    /**
     * Создание или обновление пользователя с последующим маппингом в сущность AuthorizedUser
     */
    AuthorizedUser saveAndMap(OAuth2User userDto, AuthProvider provider);

    /**
     * Создание пользователя на основе регистрационных данных. Пользователь будет не активирован.
     *
     * @param userDto данные указанные при регистрации
     */
    UserEntity saveUser(RegistrationDto userDto);

    /**
     * Активация пользователя
     *
     * @param userId   уникальный идентификатор пользователя
     * @param password пароль пользователя
     */
    UserEntity firstActivation(UUID userId, String password);

    /**
     * Создать пользователя и сразу активировать
     */
    UserEntity saveAndActivateUser(RegistrationDto userDto);

    /**
     * Проверить существует ли пользователь с указанным email
     */
    boolean existByEmail(String email);
}
