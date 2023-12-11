package ru.dlabs.sas.example.jsso.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StackElementSerializer extends JsonSerializer<StackTraceElement[]> {

    @Override
    public void serialize(
        StackTraceElement[] value,
        JsonGenerator gen,
        SerializerProvider serializer
    ) throws IOException {
        if (value != null) {
            gen.writeStartArray();
            List<String> items = Arrays.stream(value).map(StackTraceElement::toString).toList();
            for (String item : items) {
                gen.writeString(item);
            }
            gen.writeEndArray();
        }
    }
}
