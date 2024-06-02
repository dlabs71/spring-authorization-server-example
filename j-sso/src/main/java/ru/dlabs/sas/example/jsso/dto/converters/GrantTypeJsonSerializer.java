package ru.dlabs.sas.example.jsso.dto.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class GrantTypeJsonSerializer extends JsonSerializer<AuthorizationGrantType> {

    @Override
    public void serialize(
        AuthorizationGrantType value,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        if (value == null) {
            return;
        }
        jsonGenerator.writeString(value.getValue());
    }
}
