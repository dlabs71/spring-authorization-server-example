package ru.dlabs.sas.example.jsso.service;

import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface ResourceServerService {

    ResponseEntity<byte[]> getUserAvatar(UUID userId);
}
