package ru.dlabs.sas.example.jsso.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.encoder.Encode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс-обёртка над HttpServletRequest. При получении значений заголовков или параметров запроса
 * кодирует их при помощи <a href="https://owasp.org/www-project-java-encoder/">OWASP Encoding Project</a>
 *
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-10 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        return this.sanitizeValues(values);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return this.sanitizeValue(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        return parameterMap.entrySet().stream().map(entry -> {
            String[] values = entry.getValue();
            entry.setValue(this.sanitizeValues(values));
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return this.sanitizeValue(value);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> values = super.getHeaders(name);
        List<String> result = new ArrayList<>();
        while (values.hasMoreElements()) {
            String value = values.nextElement();
            result.add(this.sanitizeValue(value));
        }
        return Collections.enumeration(result);
    }

    private String[] sanitizeValues(String[] values) {
        if (values == null) {
            return null;
        }
        return Stream.of(values)
                .map(Encode::forHtmlContent)
                .toArray(String[]::new);
    }

    private String sanitizeValue(String value) {
        if (value == null) {
            return null;
        }
        return Encode.forHtmlContent(value);
    }
}
