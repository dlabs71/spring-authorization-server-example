package ru.dlabs.sas.example.jsso.components.impl;

import ru.dlabs.sas.example.jsso.components.FileStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletionException;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-04-30</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class LocalFileStore implements FileStore {

    private final Path storePath;

    public LocalFileStore(Path storePath) {
        this.storePath = storePath;
    }

    @Override
    public Long add(String bucket, String fileId, Long length, InputStream in) {
        Path path = storePath.resolve(bucket);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path targetPath = path.resolve(fileId);
            return Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    @Override
    public InputStream getFile(String bucket, String fileId) throws IOException {
        Path pathToFile = storePath.resolve(bucket).resolve(fileId);

        if (!Files.exists(pathToFile)) {
            throw new FileNotFoundException(String.format("File with fileId=[%s], bucket=[%s] not found", fileId, bucket));
        }
        return Files.newInputStream(pathToFile);
    }

    @Override
    public void delete(String bucket, String fileId) {
        Path path = storePath.resolve(bucket).resolve(fileId);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    @Override
    public Boolean isFileExist(String bucket, String fileId) {
        Path path = storePath.resolve(bucket).resolve(fileId);
        return Files.exists(path);
    }
}
