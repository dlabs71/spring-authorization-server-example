package ru.dlabs.sas.example.jsso.components;

import java.util.Map;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ConfirmationStore {

    /**
     * Сохранить данные под ключом sessionId
     *
     * @param item      данные для сохранения
     * @param sessionId ключ
     */
    void save(StoreItem item, String sessionId) throws Exception;

    /**
     * Достать и удалить данные по ключу sessionId. Если ключ не будет найден, будет возвращено null
     *
     * @param sessionId ключ
     */
    StoreItem take(String sessionId) throws Exception;

    /**
     * Данные для хранения в сторе
     *
     * @param email       email адрес пользователя
     * @param confirmCode Код подтверждения
     */
    record StoreItem(String email, String confirmCode, Map<String, String> extraData) {
    }
}
