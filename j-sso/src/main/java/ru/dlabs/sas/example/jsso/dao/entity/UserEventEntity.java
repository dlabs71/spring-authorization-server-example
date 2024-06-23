package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
@Setter
@Entity
@Table(schema = "sso", name = "user_events")
public class UserEventEntity {

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private UserEventType eventType;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "agent_browser")
    private String browser;

    @Column(name = "agent_device")
    private String device;

    @Column(name = "agent_os")
    private String os;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;
}
