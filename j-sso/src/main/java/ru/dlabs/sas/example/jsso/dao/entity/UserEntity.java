package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

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
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(schema = "sso", name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public List<RoleEntity> roles = new ArrayList<>();

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

}
