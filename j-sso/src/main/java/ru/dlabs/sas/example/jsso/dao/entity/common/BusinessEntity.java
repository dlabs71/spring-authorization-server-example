package ru.dlabs.sas.example.jsso.dao.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
@Setter
@MappedSuperclass
public abstract class BusinessEntity<Id extends Serializable> implements CoreEntity<Id> {

    public final static String DEFAULT_USER = "system";

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_updated_by", nullable = false)
    private String lastUpdatedBy;

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        this.lastUpdateDate = this.creationDate;

        if (StringUtils.isEmpty(this.createdBy)) {
            this.createdBy = findCurrentUser();
        }
        this.lastUpdatedBy = this.createdBy;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateDate = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        this.lastUpdatedBy = findCurrentUser();
    }

    private String findCurrentUser() {
        String user = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = null;
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
            user = userDetails != null ? userDetails.getUsername() : null;
        }
        return user == null ? DEFAULT_USER : user;
    }
}
