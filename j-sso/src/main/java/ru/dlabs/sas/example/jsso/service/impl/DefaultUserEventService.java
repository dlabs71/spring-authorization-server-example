package ru.dlabs.sas.example.jsso.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.UserEventEntity;
import ru.dlabs.sas.example.jsso.dao.repository.UserEventRepository;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;
import ru.dlabs.sas.example.jsso.dto.UserEventDto;
import ru.dlabs.sas.example.jsso.mapper.UserEventMapper;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.UserEventService;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;
import ua_parser.Client;
import ua_parser.Parser;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DefaultUserEventService implements UserEventService {

    private final UserEventRepository userEventRepository;
    private final MessageService messageService;

    @Value("${scheduled-tasks.delete-old-events.saved-period-days}")
    private Long savedPeriodDays;

    @Override
    @Transactional(readOnly = true)
    public PageableResponseDto<UserEventDto> searchEvents(int page, int pageSize) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<UserEventEntity> entitiesPage = userEventRepository.findAllByCreatedBy(
            authorizedUser.getUsername(),
            pageRequest
        );
        List<UserEventDto> dtoList = entitiesPage.get()
            .map(item -> UserEventMapper.map(item, messageService))
            .toList();

        return PageableResponseDto.build(
            dtoList,
            page < entitiesPage.getTotalPages(),
            entitiesPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public void createEvent(UserEventType eventType, String clientId, HttpServletRequest request) {
        UserEventEntity entity = new UserEventEntity();
        String userAgentValue = request.getHeader(HttpHeaders.USER_AGENT);
        ParserResult parseResult = this.parseUserAgent(userAgentValue);

        entity.setEventType(eventType);
        entity.setUserAgent(userAgentValue);
        entity.setIpAddress(request.getRemoteAddr());
        entity.setClientId(clientId);
        entity.setCreatedBy(SecurityUtils.getAuthUser().getEmail());
        entity.setCreationDate(LocalDateTime.now());
        entity.setBrowser(parseResult.browser());
        entity.setDevice(parseResult.device());
        entity.setOs(parseResult.os());
        userEventRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteOldEvents() {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.minusDays(savedPeriodDays);
        userEventRepository.deleteAllLessThenCreationDate(threshold);
    }

    private ParserResult parseUserAgent(String userAgentHeaderValue) {
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentHeaderValue);

        String browser = null;
        if (client.userAgent != null) {
            String majorVersion = client.userAgent.major != null ? client.userAgent.major : "";
            browser = client.userAgent.family + " " + majorVersion;
            browser = browser.trim();
        }

        String device = null;
        if (client.device != null) {
            device = client.device.family;
        }

        String os = null;
        if (client.os != null) {
            String majorVersion = client.os.major != null ? client.os.major : "";
            os = client.os.family + " " + majorVersion;
            os = os.trim();
        }

        return new ParserResult(browser, device, os);
    }

    private record ParserResult(String browser, String device, String os) {
    }
}
