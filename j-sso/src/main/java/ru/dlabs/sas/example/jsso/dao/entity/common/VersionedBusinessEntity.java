package ru.dlabs.sas.example.jsso.dao.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class VersionedBusinessEntity<Id extends Serializable> extends BusinessEntity<Id> {

    @Version
    @Column(name = "object_version_number", nullable = false)
    private Long version;

}
