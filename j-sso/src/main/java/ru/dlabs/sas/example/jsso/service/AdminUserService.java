package ru.dlabs.sas.example.jsso.service;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import ru.dlabs.sas.example.jsso.dto.AdminUserDto;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface AdminUserService {

    PageableResponseDto<AdminUserDto> searchUsers(int page, int pageSize, String email);

    void assignAdmin(String email);

    void dismissAdmin(UUID userId);

    ResponseEntity<byte[]> getAvatar(UUID avatarFileId);
}
