package ru.dlabs.sas.example.jsso.dao.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dlabs.sas.example.jsso.dao.entity.FileStoreEntity;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-04-30</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Repository
public interface FileStoreRepository extends JpaRepository<FileStoreEntity, UUID> {
}
