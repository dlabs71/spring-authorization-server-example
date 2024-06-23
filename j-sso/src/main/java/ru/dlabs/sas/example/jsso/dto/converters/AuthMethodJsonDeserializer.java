package ru.dlabs.sas.example.jsso.dto.converters;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class AuthMethodJsonDeserializer extends JsonDeserializer<ClientAuthenticationMethod> {

    @Override
    public ClientAuthenticationMethod deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        String strValue = jsonParser.getText();
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }
        return new ClientAuthenticationMethod(strValue);
    }
}
