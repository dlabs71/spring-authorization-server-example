package ru.dlabs.sas.example.jsso.mapper;

import ru.dlabs.sas.example.jsso.dao.entity.FileStoreEntity;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-04-30</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class FileStoreMapper {

    public static FileStoreDto map(FileStoreEntity entity) {
        return FileStoreDto.builder()
            .bucket(entity.getBucket())
            .contentType(entity.getContentType())
            .id(entity.getId())
            .name(entity.getFilename())
            .size(entity.getFileSize())
            .build();
    }
}
