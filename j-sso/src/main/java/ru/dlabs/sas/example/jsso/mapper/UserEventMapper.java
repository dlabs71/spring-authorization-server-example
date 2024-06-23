package ru.dlabs.sas.example.jsso.mapper;

import lombok.experimental.UtilityClass;
import ru.dlabs.sas.example.jsso.dao.entity.UserEventEntity;
import ru.dlabs.sas.example.jsso.dto.UserEventDto;
import ru.dlabs.sas.example.jsso.service.MessageService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@UtilityClass
public class UserEventMapper {

    public UserEventDto map(UserEventEntity entity, MessageService messageService) {
        return UserEventDto.builder()
            .id(entity.getId())
            .eventType(entity.getEventType())
            .eventTypeName(messageService.getMessage(entity.getEventType()))
            .ipAddress(entity.getIpAddress())
            .clientId(entity.getClientId())
            .browser(entity.getBrowser())
            .device(entity.getDevice())
            .os(entity.getOs())
            .creationDate(entity.getCreationDate())
            .build();
    }
}
