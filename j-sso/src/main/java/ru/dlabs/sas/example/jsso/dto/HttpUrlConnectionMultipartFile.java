package ru.dlabs.sas.example.jsso.dto;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class HttpUrlConnectionMultipartFile implements MultipartFile {

    private final String name;
    private final String contentType;
    private final int contentLength;
    private final byte[] content;

    public HttpUrlConnectionMultipartFile(HttpURLConnection connection) throws IOException {
        this.name = UUID.randomUUID().toString();
        this.contentType = connection.getContentType();
        this.contentLength = connection.getContentLength();
        try (InputStream is = connection.getInputStream()) {
            this.content = is.readAllBytes();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.name;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.contentLength == 0;
    }

    @Override
    public long getSize() {
        return this.contentLength;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
