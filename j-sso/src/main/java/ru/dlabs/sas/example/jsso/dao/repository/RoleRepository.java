package ru.dlabs.sas.example.jsso.dao.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dlabs.sas.example.jsso.dao.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("select r from RoleEntity r where r.code = 'USER_SSO' and r.systemCode = 'SSO'")
    RoleEntity getDefaultRole();

    @Query("select r from RoleEntity r where r.code = 'ADMIN_SSO' and r.systemCode = 'SSO'")
    RoleEntity getAdminRole();
}
