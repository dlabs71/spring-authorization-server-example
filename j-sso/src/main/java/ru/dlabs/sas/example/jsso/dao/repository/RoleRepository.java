package ru.dlabs.sas.example.jsso.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dlabs.sas.example.jsso.dao.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("select r from RoleEntity r where r.code = 'USER_SSO'")
    RoleEntity getDefaultRole();
}
