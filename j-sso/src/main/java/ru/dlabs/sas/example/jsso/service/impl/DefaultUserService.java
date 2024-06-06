package ru.dlabs.sas.example.jsso.service.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.RoleRepository;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.exception.InformationException;
import ru.dlabs.sas.example.jsso.exception.RegistrationException;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.service.FileStoreService;
import ru.dlabs.sas.example.jsso.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStoreService fileStoreService;

    /**
     * Создание пользователя на основе регистрационных данных. Пользователь будет не активирован.
     *
     * @param userDto данные указанные при регистрации
     */
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
        user.setAdmin(false);
        user.setSuperuser(false);
        user.getRoles().add(roleRepository.getDefaultRole());
        return userRepository.save(user);
    }

    /**
     * Валидация регистрационных данных
     */
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

    /**
     * Активация пользователя
     *
     * @param userId   уникальный идентификатор пользователя
     * @param password пароль пользователя
     */
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
        userEntity.setAdmin(false);
        return userRepository.save(userEntity);
    }

    /**
     * Создать пользователя и сразу активировать
     */
    @Override
    @Transactional
    public UserEntity saveAndActivateUser(RegistrationDto userDto) {
        UserEntity user = this.saveUser(userDto);
        user = this.firstActivation(user.getId(), userDto.getPassword());
        return user;
    }

    /**
     * Проверить существует ли пользователь с указанным email
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Найти entity пользователя по email
     */
    @Override
    @Transactional(readOnly = true)
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Сменить пароль у пользователя с указанным email
     */
    @Override
    @Transactional
    public void changePassword(String email, String password) {
        UserEntity user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new RegistrationException("$user.not.found");
        }
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public UserAvatar getUserAvatar(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        if (userEntity == null || userEntity.getAvatarFileId() == null) {
            throw ServiceException.builder("$avatar.not.found").build();
        }
        UUID avatarFileId = userEntity.getAvatarFileId();
        FileStoreDto fileStoreDto = fileStoreService.getById(avatarFileId);
        if (fileStoreDto == null) {
            throw ServiceException.builder("$avatar.not.found").build();
        }

        try {
            byte[] avatar = fileStoreService.download(avatarFileId);
            return new UserAvatar(fileStoreDto, avatar);
        } catch (IOException | InformationException | ServiceException e) {
            log.error(e.getMessage(), e);
            throw ServiceException.builder("$avatar.not.found").build();
        }
    }
}
