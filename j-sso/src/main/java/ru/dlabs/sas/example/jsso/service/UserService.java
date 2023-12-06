package ru.dlabs.sas.example.jsso.service;

import java.util.UUID;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

public interface UserService {

    UserEntity save(OAuth2User userDto, AuthProvider provider);

    AuthorizedUser saveAndMap(OAuth2User userDto, AuthProvider provider);

    UserEntity saveUser(RegistrationDto userDto);

    UserEntity firstActivation(UUID userId, String password);

    UserEntity saveAndActivateUser(RegistrationDto userDto);

    void changePassword(String email, String passwordHash);

    boolean existByEmail(String email);

    UserEntity findByEmail(String email);
}
