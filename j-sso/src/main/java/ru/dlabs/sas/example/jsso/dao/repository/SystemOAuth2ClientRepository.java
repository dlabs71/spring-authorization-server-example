package ru.dlabs.sas.example.jsso.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface SystemOAuth2ClientRepository
    extends JpaRepository<SystemOauth2Client, String>, JpaSpecificationExecutor<SystemOauth2Client> {

    SystemOauth2Client getByClientId(String clientId);
}
