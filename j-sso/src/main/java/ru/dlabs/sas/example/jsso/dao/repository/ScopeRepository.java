package ru.dlabs.sas.example.jsso.dao.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dlabs.sas.example.jsso.dao.entity.ScopeEntity;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-16</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ScopeRepository extends JpaRepository<ScopeEntity, UUID> {

}
