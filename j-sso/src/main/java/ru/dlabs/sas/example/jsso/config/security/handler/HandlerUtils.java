package ru.dlabs.sas.example.jsso.config.security.handler;

import lombok.experimental.UtilityClass;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-22</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@UtilityClass
public class HandlerUtils {

    /**
     * Получение clientId из сохранённого запроса.
     */
    public String getClientId(SavedRequest savedRequest) {
        if (savedRequest != null) {
            if (savedRequest.getParameterMap().containsKey("client_id")) {
                String[] clientIdValues = savedRequest.getParameterValues("client_id");
                if (clientIdValues.length > 0) {
                    return clientIdValues[0];
                }
            }
        }
        return null;
    }
}
