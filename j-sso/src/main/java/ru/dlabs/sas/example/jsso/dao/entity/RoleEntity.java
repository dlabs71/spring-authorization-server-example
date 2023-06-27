package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "roles")
public class RoleEntity extends VersionedBusinessEntity<String> {

    @Id
    @Column(name = "role_code", nullable = false)
    private String code;
    @Column(name = "role_description", nullable = false)
    private String description;
    @Column(name = "active")
    private Boolean active;

    @ManyToMany
    @JoinTable(schema = "sso", name = "role_authorities",
            joinColumns = @JoinColumn(name = "role_code"),
            inverseJoinColumns = @JoinColumn(name = "authority_code")
    )
    public List<AuthorityEntity> authorities;

    @Override
    public String getId() {
        return this.code;
    }

    @Override
    public void setId(String code) {
        this.code = code;
    }

}
