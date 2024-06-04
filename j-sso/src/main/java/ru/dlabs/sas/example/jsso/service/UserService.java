package ru.dlabs.sas.example.jsso.service;

import java.util.UUID;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public interface UserService {

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

    /**
     * Найти entity пользователя по email
     */
    UserEntity findByEmail(String email);

    /**
     * Сменить пароль у пользователя с указанным email
     */
    void changePassword(String email, String password);

    /**
     * Получение аватара пользователя
     */
    UserAvatar getUserAvatar(UUID userId);

    record UserAvatar(FileStoreDto storeDto, byte[] avatar) { }
}
