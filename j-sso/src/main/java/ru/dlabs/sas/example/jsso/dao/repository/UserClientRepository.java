package ru.dlabs.sas.example.jsso.dao.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dlabs.sas.example.jsso.dao.entity.UserClient;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-12</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface UserClientRepository extends JpaRepository<UserClient, String> {

    UserClient findByUserIdAndClientId(UUID userId, String clientId);

    List<UserClient> findByUserId(UUID userId);

    List<UserClient> findAllByDeletedIsTrue();

    void deleteAllByDeletedIsTrue();

}
