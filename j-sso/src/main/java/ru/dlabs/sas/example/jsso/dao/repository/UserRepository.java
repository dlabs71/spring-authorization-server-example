package ru.dlabs.sas.example.jsso.dao.repository;

import org.springframework.stereotype.Repository;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {

    private final Map<UUID, UserEntity> store = new HashMap<>();

    public UserEntity save(UserEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        this.store.put(entity.getId(), entity);
        return entity;
    }

    public UserEntity findById(UUID id) {
        return this.store.getOrDefault(id, null);
    }

    public UserEntity findByEmail(String email) {
        return this.store.values().stream()
                .filter(item -> item.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
