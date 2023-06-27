package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "authorities")
public class AuthorityEntity extends VersionedBusinessEntity<String> {

    @Id
    @Column(name = "authority_code", nullable = false)
    private String code;

    @Column(name = "authority_description", nullable = false)
    private String description;

    @Column(name = "system_code", nullable = false)
    private String systemCode;

    @Column(name = "active")
    private Boolean active;

    @Override
    public String getId() {
        return this.code;
    }

    @Override
    public void setId(String code) {
        this.code = code;
    }

}
