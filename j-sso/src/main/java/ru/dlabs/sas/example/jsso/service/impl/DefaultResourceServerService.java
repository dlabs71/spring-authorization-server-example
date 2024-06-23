package ru.dlabs.sas.example.jsso.service.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.service.ResourceServerService;
import ru.dlabs.sas.example.jsso.service.UserService;
import ru.dlabs.sas.example.jsso.utils.HttpUtils;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-03 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultResourceServerService implements ResourceServerService {

    private final UserService userService;

    @Override
    public ResponseEntity<byte[]> getUserAvatar(UUID userId) {
        try {
            UserService.UserAvatar userAvatar = userService.getUserAvatar(userId);
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
