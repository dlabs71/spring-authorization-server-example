package ru.dlabs.sas.example.jsso.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dao.type.StoreType;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.dto.UserDto;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.mapper.AuthorizedUserMapper;
import ru.dlabs.sas.example.jsso.mapper.UserDtoMapper;
import ru.dlabs.sas.example.jsso.service.AccountService;
import ru.dlabs.sas.example.jsso.service.FileStoreService;
import ru.dlabs.sas.example.jsso.service.UserClientService;
import ru.dlabs.sas.example.jsso.service.UserEventService;
import ru.dlabs.sas.example.jsso.service.UserService;
import ru.dlabs.sas.example.jsso.service.UserTokenService;
import ru.dlabs.sas.example.jsso.service.security.SecurityService;
import ru.dlabs.sas.example.jsso.utils.HttpUtils;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {

    private final UserRepository userRepository;
    private final FileStoreService fileStoreService;
    private final SecurityService securityService;
    private final UserTokenService userTokenService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;
    private final UserClientService userClientService;
    private final UserEventService eventService;
    private final UserService userService;
    private final RedisIndexedSessionRepository sessionRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        UserEntity entity = userRepository.getReferenceById(authorizedUser.getId());
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public UserDto save(
        UserDto dto,
        MultipartFile avatarFile,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Optional<UserEntity> entityWrapper = userRepository.findById(dto.getId());
        if (entityWrapper.isEmpty()) {
            throw ServiceException.builder("Entity not found").build();
        }
        UserEntity entity = entityWrapper.get();

        if (avatarFile != null && !avatarFile.isEmpty()) {
            FileStoreDto fileStoreDto = fileStoreService.saveOrReplace(
                avatarFile,
                StoreType.AVATAR,
                entity.getAvatarFileId()
            );
            entity.setAvatarFileId(fileStoreDto != null ? fileStoreDto.getId() : null);
        }

        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthday(dto.getBirthday());
        entity = userRepository.save(entity);

        AuthorizedUser updatedAuthorizedUser = AuthorizedUserMapper.reload(SecurityUtils.getAuthUser(), entity);
        securityService.reloadSecurityContext(updatedAuthorizedUser, request, response);
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.invalidateAllSessionExceptCurrent(authorizedUser);
        userTokenService.recallAllCurrentUserTokens();
        userClientService.markAsDelete(authorizedUser.getId());
        fileStoreService.delete(authorizedUser.getAvatarFileId());
        userRepository.deleteById(authorizedUser.getId());
        eventService.createEvent(UserEventType.USER_DELETE, null, request);
        securityContextLogoutHandler.logout(request, response, authentication);
    }

    private void invalidateAllSessionExceptCurrent(AuthorizedUser authorizedUser) {
        String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(authorizedUser.getUsername());
        sessions.forEach((sessionId, session) -> {
            if (!sessionId.equals(currentSessionId)) {
                sessionRepository.deleteById(sessionId);
            }
        });
    }

    @Override
    public ResponseEntity<byte[]> getAvatarCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        try {
            UserService.UserAvatar userAvatar = userService.getUserAvatar(authorizedUser.getId());
            return HttpUtils.appendFileToResponse(
                userAvatar.storeDto().getId().toString(),
                userAvatar.storeDto().getContentType(),
                userAvatar.avatar()
            );
        } catch (ServiceException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
