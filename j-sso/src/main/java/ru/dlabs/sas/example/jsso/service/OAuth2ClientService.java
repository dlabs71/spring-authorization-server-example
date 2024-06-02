package ru.dlabs.sas.example.jsso.service;

import java.util.Set;
import java.util.UUID;
import ru.dlabs.sas.example.jsso.dto.OAuth2ClientDto;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface OAuth2ClientService {

    OAuth2ClientDto getByClientId(String clientId);

    PageableResponseDto<OAuth2ClientDto> searchClients(int page, int limit, String clientId, String clientName);

    OAuth2ClientDto save(OAuth2ClientDto dto);

    void delete(String clientId);

    String generateSecret(String clientId);

    /**
     * Уведомить всех клиентов об удалении пользователя(-ей), которые помечены в таблице user_clients как удалённые.
     */
    void notifyClientsAndClear();

    /**
     * Уведомить клиента с указанным clientId, что были удалены пользователи с указанными идентификаторами.
     */
    void notifyClientForDeleteAccount(String clientId, Set<UUID> userIds);
}
