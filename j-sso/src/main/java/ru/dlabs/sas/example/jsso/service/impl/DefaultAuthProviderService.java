package ru.dlabs.sas.example.jsso.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.RoleRepository;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dao.type.StoreType;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.exception.AuthException;
import ru.dlabs.sas.example.jsso.mapper.AuthorizedUserMapper;
import ru.dlabs.sas.example.jsso.service.AuthProviderService;
import ru.dlabs.sas.example.jsso.service.FileStoreService;
import ru.dlabs.sas.example.jsso.type.AuthErrorCode;
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
@Service
@RequiredArgsConstructor
public class DefaultAuthProviderService implements AuthProviderService {

    @Value("${yandex-avatar-url}")
    private String yandexAvatarUrl;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FileStoreService fileStoreService;

    /**
     * Создание или обновление пользователя используя сервис-провайдер
     */
    @Override
    public UserEntity save(OAuth2User userDto, AuthProvider provider) {
        return switch (provider) {
            case GITHUB -> this.saveUserFromGithab(userDto);
            case GOOGLE -> this.saveUserFromGoogle(userDto);
            case YANDEX -> this.saveUserFromYandex(userDto);
        };
    }

    /**
     * Создание или обновление пользователя с последующим маппингом в сущность AuthorizedUser
     */
    @Override
    public AuthorizedUser saveAndMap(OAuth2User userDto, AuthProvider provider) {
        UserEntity entity = this.save(userDto, provider);
        return AuthorizedUserMapper.map(entity, provider);
    }


    /**
     * Метод описывающий создание/обновление UserEntity на основе OAuth2User полученного из провайдера Github
     */
    private UserEntity saveUserFromGithab(OAuth2User userDto) {
        String email = userDto.getAttribute("email");
        UserEntity user = this.getEntityByEmail(email);

        // Не обновляем пользователя если он уже существует в БД
        if (user.getId() == null) {
            if (userDto.getAttribute("name") != null) {
                String[] splitted = ((String) userDto.getAttribute("name")).split(" ");
                user.setFirstName(splitted[0]);
                if (splitted.length > 1) {
                    user.setLastName(splitted[1]);
                }
                if (splitted.length > 2) {
                    user.setMiddleName(splitted[2]);
                }
            } else {
                user.setFirstName(userDto.getAttribute("login"));
                user.setLastName(userDto.getAttribute("login"));
            }

            if (userDto.getAttribute("avatar_url") != null) {
                String avatarUrl = userDto.getAttribute("avatar_url");
                user.setAvatarFileId(this.createAvatar(avatarUrl));
            }
            user = userRepository.save(user);
        }
        return user;
    }

    /**
     * Метод описывающий создание/обновление UserEntity на основе OAuth2User полученного из провайдера Google
     */
    private UserEntity saveUserFromGoogle(OAuth2User userDto) {
        String email = userDto.getAttribute("email");
        UserEntity user = this.getEntityByEmail(email);

        // Не обновляем пользователя если он уже существует в БД
        if (user.getId() == null) {
            if (userDto.getAttribute("given_name") != null) {
                user.setFirstName(userDto.getAttribute("given_name"));
            }

            if (userDto.getAttribute("family_name") != null) {
                user.setLastName(userDto.getAttribute("family_name"));
            }

            if (userDto.getAttribute("picture") != null) {
                String avatarUrl = userDto.getAttribute("picture");
                user.setAvatarFileId(this.createAvatar(avatarUrl));
            }

            user = userRepository.save(user);
        }
        return user;
    }

    /**
     * Метод описывающий создание/обновление UserEntity на основе OAuth2User полученного из провайдера Yandex
     */
    private UserEntity saveUserFromYandex(OAuth2User userDto) {
        String email = userDto.getAttribute("default_email");
        UserEntity user = this.getEntityByEmail(email);

        // Не обновляем пользователя если он уже существует в БД
        if (user.getId() == null) {
            if (userDto.getAttribute("first_name") != null) {
                user.setFirstName(userDto.getAttribute("first_name"));
            }

            if (userDto.getAttribute("last_name") != null) {
                user.setLastName(userDto.getAttribute("last_name"));
            }

            if (userDto.getAttribute("default_avatar_id") != null) {
                String attrValue = userDto.getAttribute("default_avatar_id");
                String avatarUrl = this.createAvatarUrl(AuthProvider.YANDEX, attrValue);
                user.setAvatarFileId(this.createAvatar(avatarUrl));
            }
            if (userDto.getAttribute("birthday") != null) {
                LocalDate birthdate = LocalDate.parse(
                    userDto.getAttribute("birthday"),
                    DateTimeFormatter.ISO_LOCAL_DATE
                );
                user.setBirthday(birthdate);
            }

            user = userRepository.save(user);
        }
        return user;
    }

    /**
     * Метод получения сущности UserEntity по email
     * Если пользователь с данным email не найден в БД, то создаём новую сущность
     */
    private UserEntity getEntityByEmail(String email) {
        if (email == null) {
            throw new AuthException(AuthErrorCode.EMAIL_IS_EMPTY);
        }
        email = email.trim().toLowerCase();
        UserEntity user = this.userRepository.findByEmail(email);
        if (user == null) {
            user = new UserEntity();
            user.setEmail(email);
            user.setActive(true);
            user.setAdmin(false);
            user.setSuperuser(false);
            // добавляем роль по умолчанию
            user.setRoles(List.of(roleRepository.getDefaultRole()));
        }
        return user;
    }

    private String createAvatarUrl(AuthProvider authProvider, String valueAvatarAttr) {
        return switch (authProvider) {
            case YANDEX -> yandexAvatarUrl.replace("{avatarId}", valueAvatarAttr);
            case GITHUB, GOOGLE -> valueAvatarAttr;
        };
    }

    private UUID createAvatar(String avatarUrl) {
        FileStoreDto fileStoreDto = fileStoreService.saveOrReplace(avatarUrl, StoreType.AVATAR, null);
        return fileStoreDto.getId();
    }
}
