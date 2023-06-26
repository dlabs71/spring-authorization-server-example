package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "users")
public class UserEntity extends VersionedBusinessEntity<UUID> {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "second_name", nullable = false)
    private String secondName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

}
