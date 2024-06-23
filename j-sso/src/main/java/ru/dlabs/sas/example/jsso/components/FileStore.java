package ru.dlabs.sas.example.jsso.components;

import java.io.IOException;
import java.io.InputStream;

public interface FileStore {

    Long add(String bucket, String fileId, Long length, InputStream in);

    InputStream getFile(String bucket, String fileId) throws IOException;

    void delete(String bucket, String fileId);

    Boolean isFileExist(String bucket, String fileId);

}
