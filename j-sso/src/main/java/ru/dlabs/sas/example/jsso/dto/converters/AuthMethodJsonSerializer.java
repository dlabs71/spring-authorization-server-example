package ru.dlabs.sas.example.jsso.dto.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
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
public class AuthMethodJsonSerializer extends JsonSerializer<ClientAuthenticationMethod> {

    @Override
    public void serialize(ClientAuthenticationMethod value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        if (value == null) {
            return;
        }
        gen.writeString(value.getValue());
    }
}
