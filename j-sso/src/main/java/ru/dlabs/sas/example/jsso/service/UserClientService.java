package ru.dlabs.sas.example.jsso.service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-12</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface UserClientService {

    void save(UUID userId, String clientId);

    /**
     * Пометить пользователя в таблице user_clients как удалённого.
     */
    void markAsDelete(UUID userId);

    /**
     * Получить ассоциативный список ключом которого является идентификатор OAuth2 клиента,
     * а значением список удалённых пользователей.
     */
    Map<String, Set<UUID>> getDeletedClientsAndUserIds();

    /**
     * Удалить записи в таблице user_clients, которые помечены как удалённые.
     */
    void deleteRowsMarkedDeleted();
}
