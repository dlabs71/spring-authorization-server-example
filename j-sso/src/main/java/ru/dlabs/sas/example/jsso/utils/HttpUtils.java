package ru.dlabs.sas.example.jsso.utils;

import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-12</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@UtilityClass
public class HttpUtils {

    /**
     * Сформировать HTTP ответ на запрос получения файла.
     */
    public ResponseEntity<byte[]> appendFileToResponse(String fileName, String contentType, byte[] bytes) {
        if (StringUtils.isEmpty(contentType)) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        final HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
            .filename(fileName, StandardCharsets.UTF_8)
            .build();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        headers.add("X-Frame-Options", "SAMEORIGIN");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
