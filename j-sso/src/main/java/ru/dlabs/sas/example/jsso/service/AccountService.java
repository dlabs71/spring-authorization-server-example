package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.dlabs.sas.example.jsso.dto.UserDto;

public interface AccountService {

    UserDto getCurrentUser();

    UserDto save(UserDto dto, MultipartFile avatarFile, HttpServletRequest request, HttpServletResponse response);

    void deleteCurrentUser(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<byte[]> getAvatarCurrentUser();
}
