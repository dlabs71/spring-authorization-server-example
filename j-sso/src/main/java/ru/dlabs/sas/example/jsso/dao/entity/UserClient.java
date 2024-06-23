package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "user_clients")
public class UserClient extends VersionedBusinessEntity<UUID> {

    @Id
    @Column(name = "user_client_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "deleted")
    private Boolean deleted;

}
