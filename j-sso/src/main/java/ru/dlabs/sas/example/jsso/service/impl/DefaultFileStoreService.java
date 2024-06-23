package ru.dlabs.sas.example.jsso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.dlabs.sas.example.jsso.components.FileStore;
import ru.dlabs.sas.example.jsso.dao.entity.FileStoreEntity;
import ru.dlabs.sas.example.jsso.dao.repository.FileStoreRepository;
import ru.dlabs.sas.example.jsso.dao.type.StoreType;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.dto.HttpUrlConnectionMultipartFile;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.mapper.FileStoreMapper;
import ru.dlabs.sas.example.jsso.service.FileStoreService;
import ru.dlabs.sas.example.jsso.service.MessageService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-04-30</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFileStoreService implements FileStoreService {

    private final FileStore fileStore;
    private final FileStoreRepository fileStoreRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public FileStoreDto saveOrReplace(MultipartFile file, StoreType storeType, UUID existedFileId) {
        FileStoreEntity entity = new FileStoreEntity();
        if (existedFileId != null) {
            entity = fileStoreRepository.getReferenceById(existedFileId);
        }
        String filename = this.getFilename(file);
        entity.setFilename(filename);
        entity.setFileSize(file.getSize());
        entity.setContentType(file.getContentType());
        entity.setBucket(storeType.getBucket());
        entity.setType(storeType);
        entity = fileStoreRepository.save(entity);

        try (InputStream fileInputStream = file.getInputStream()) {
            fileStore.add(entity.getBucket(), String.valueOf(entity.getId()), entity.getFileSize(), fileInputStream);
        } catch (IOException e) {
            String errorMessage = String.format("An error occurred while saving the file to storage. File name [%s]. Message: %s",
                                                entity.getFilename(),
                                                e.getMessage()
            );
            log.error(errorMessage, e);
            throw ServiceException.builder(messageService.getMessage("file.store.error", filename)).build();
        }

        return FileStoreMapper.map(entity);
    }

    @Override
    @Transactional
    public FileStoreDto saveOrReplace(String externalUrl, StoreType storeType, UUID existedFileId) {
        try {
            URL url = new URL(externalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            MultipartFile multipartFile = new HttpUrlConnectionMultipartFile(connection);
            connection.disconnect();
            return this.saveOrReplace(multipartFile, storeType, existedFileId);
        } catch (IOException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public FileStoreDto getById(UUID fileId) {
        FileStoreEntity entity = fileStoreRepository.getReferenceById(fileId);
        return FileStoreMapper.map(entity);
    }

    @Override
    @Transactional
    public void delete(UUID fileId) {
        deleteFile(fileId);
    }

    @Override
    @Transactional
    public void deleteList(List<UUID> fileIds) {
        fileIds.forEach(this::deleteFile);
    }

    private void deleteFile(UUID id) {
        if (id == null) {
            return;
        }
        FileStoreEntity fileEntity = fileStoreRepository.getReferenceById(id);
        fileStore.delete(fileEntity.getBucket(), String.valueOf(fileEntity.getId()));
        fileStoreRepository.delete(fileEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] download(UUID fileId) throws IOException {
        FileStoreEntity entity = fileStoreRepository.getReferenceById(fileId);

        if (!fileStore.isFileExist(entity.getBucket(), String.valueOf(entity.getId()))) {
            throw ServiceException.builder(messageService.getMessage("file.store.not.found", fileId)).build();
        }

        try (InputStream fileInputStream = fileStore.getFile(entity.getBucket(), String.valueOf(entity.getId()));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copyLarge(fileInputStream, outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public Boolean isExists(UUID fileId, StoreType storeType) {
        return fileStore.isFileExist(storeType.getBucket(), fileId.toString());
    }

    private String getFilename(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            return originalFilename;
        }
        return file.getName();
    }
}
