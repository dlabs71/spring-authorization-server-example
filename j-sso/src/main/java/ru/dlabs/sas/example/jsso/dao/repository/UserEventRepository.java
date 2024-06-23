package ru.dlabs.sas.example.jsso.dao.repository;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.dlabs.sas.example.jsso.dao.entity.UserEventEntity;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface UserEventRepository extends JpaRepository<UserEventEntity, UUID> {

    @Modifying
    @Query(value = "delete from sso.user_events where date(created_date) < :threshold", nativeQuery = true)
    void deleteAllLessThenCreationDate(LocalDate threshold);

    Page<UserEventEntity> findAllByCreatedBy(String username, PageRequest pageRequest);
}
