package ru.dlabs.sas.example.jsso.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.RoleRepository;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.exception.AuthException;
import ru.dlabs.sas.example.jsso.exception.RegistrationException;
import ru.dlabs.sas.example.jsso.mapper.AuthorizedUserMapper;
import ru.dlabs.sas.example.jsso.type.AuthErrorCode;
import ru.dlabs.sas.example.jsso.type.AuthProvider;


@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    @Value("${yandex-avatar-url}")
    private String yandexAvatarUrl;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создание или обновление пользователя
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
        String email = userDto.getAttribute("email");           // пытаемся получить атрибут email
        UserEntity user = this.getEntityByEmail(email);

        if (userDto.getAttribute("name") != null) {             // получаем firstName, lastName и middleName
            String[] splitted = ((String) userDto.getAttribute("name")).split(" ");
            user.setFirstName(splitted[0]);
            if (splitted.length > 1) {
                user.setLastName(splitted[1]);
            }
            if (splitted.length > 2) {
                user.setMiddleName(splitted[2]);
            }
        } else {                                                      // иначе устанавливаем в эти поля значение email
            user.setFirstName(userDto.getAttribute("login"));   // конечно в реальных проектах так делать не надо, здесь это сделано для упрощения логики
            user.setLastName(userDto.getAttribute("login"));
        }

        if (userDto.getAttribute("avatar_url") !=
            null) {       // если есть аватар, то устанавливаем значение в поле avatarUrl
            user.setAvatarUrl(userDto.getAttribute("avatar_url"));
        }
        return userRepository.save(user);                             // сохраняем сущность UserEntity
    }

    /**
     * Метод описывающий создание/обновление UserEntity на основе OAuth2User полученного из провайдера Google
     */
    private UserEntity saveUserFromGoogle(OAuth2User userDto) {
        String email = userDto.getAttribute("email");
        UserEntity user = this.getEntityByEmail(email);

        if (userDto.getAttribute("given_name") != null) {
            user.setFirstName(userDto.getAttribute("given_name"));
        }

        if (userDto.getAttribute("family_name") != null) {
            user.setLastName(userDto.getAttribute("family_name"));
        }

        if (userDto.getAttribute("picture") != null) {
            user.setAvatarUrl(userDto.getAttribute("picture"));
        }

        return userRepository.save(user);
    }

    /**
     * Метод описывающий создание/обновление UserEntity на основе OAuth2User полученного из провайдера Yandex
     */
    private UserEntity saveUserFromYandex(OAuth2User userDto) {
        String email = userDto.getAttribute("default_email");
        UserEntity user = this.getEntityByEmail(email);

        if (userDto.getAttribute("first_name") != null) {
            user.setFirstName(userDto.getAttribute("first_name"));
        }

        if (userDto.getAttribute("last_name") != null) {
            user.setLastName(userDto.getAttribute("last_name"));
        }

        if (userDto.getAttribute("default_avatar_id") != null) {
            user.setAvatarUrl(this.createYandexAvatarUrl(userDto.getAttribute("default_avatar_id")));
        }
        if (userDto.getAttribute("birthday") != null) {
            user.setBirthday(LocalDate.parse(userDto.getAttribute("birthday"), DateTimeFormatter.ISO_LOCAL_DATE));
        }

        return userRepository.save(user);
    }

    private String createYandexAvatarUrl(String avatarId) {
        return yandexAvatarUrl.replace("{avatarId}", avatarId);
    }

    /**
     * Метод получения сущности UserEntity по email
     * Если пользователь с данным email не найден в БД, то создаём новую сущность
     */
    private UserEntity getEntityByEmail(String email) {
        if (email == null) {
            throw new AuthException(AuthErrorCode.EMAIL_IS_EMPTY);
        }
        UserEntity user = this.userRepository.findByEmail(email);
        if (user == null) {
            user = new UserEntity();
            user.setEmail(email);
            user.setActive(true);
            // добавляем роль по умолчанию
            user.setRoles(List.of(roleRepository.getDefaultRole()));
        }
        return user;
    }

    @Override
    @Transactional
    public UserEntity saveUser(RegistrationDto userDto) {
        this.validateRegistrationDto(userDto);
        UserEntity user = new UserEntity();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getSecondName());
        user.setMiddleName(userDto.getMiddleName());
        user.setBirthday(userDto.getBirthday());
        user.setActive(false);
        user.getRoles().add(roleRepository.getDefaultRole());
        return userRepository.save(user);
    }

    private void validateRegistrationDto(RegistrationDto dto) {
        if (dto.getEmail() == null) {
            throw new RegistrationException("$validation.email");
        }
        if (dto.getFirstName() == null) {
            throw new RegistrationException("$validation.firstname");
        }
        if (dto.getSecondName() == null) {
            throw new RegistrationException("$validation.lastname");
        }
        if (dto.getPassword() == null) {
            throw new RegistrationException("$validation.password");
        }

        UserEntity user = this.userRepository.findByEmail(dto.getEmail());
        if (user != null) {
            throw new RegistrationException("$account.already.exist");
        }
    }

    @Override
    @Transactional
    public UserEntity firstActivation(UUID userId, String password) {
        Optional<UserEntity> userEntityOptional = this.userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new RegistrationException("$user.not.found");
        }
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setPasswordHash(passwordEncoder.encode(password));
        userEntity.setActive(true);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity saveAndActivateUser(RegistrationDto userDto) {
        UserEntity user = this.saveUser(userDto);
        user = this.firstActivation(user.getId(), userDto.getPassword());
        return user;
    }

    @Override
    public void changePassword(String email, String passwordHash) {
        UserEntity user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new RegistrationException("$user.not.found");
        }
        user.setPasswordHash(passwordEncoder.encode(passwordHash));
        userRepository.save(user);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
