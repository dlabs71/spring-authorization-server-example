package ru.dlabs.sas.example.jsso.dao.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dlabs.sas.example.jsso.dao.entity.ScopeVWEntity;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-16</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ScopeVWRepository extends JpaRepository<ScopeVWEntity, UUID> {

    ScopeVWEntity findByUniqueCode(String uniqueCode);

    List<ScopeVWEntity> findAllByUniqueCodeIn(Collection<String> uniqueCodes);
}
