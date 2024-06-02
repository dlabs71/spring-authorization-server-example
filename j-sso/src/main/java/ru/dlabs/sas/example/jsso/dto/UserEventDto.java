package ru.dlabs.sas.example.jsso.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDto {

    private UUID id;
    private UserEventType eventType;
    private String eventTypeName;
    private String ipAddress;
    private String clientId;
    private String browser;
    private String device;
    private String os;
    private LocalDateTime creationDate;
}
