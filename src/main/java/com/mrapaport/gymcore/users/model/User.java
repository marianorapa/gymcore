package com.mrapaport.gymcore.users.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.usage.UsageService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(name = "pin", unique = true, nullable = false)
    private Integer pin;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "dni", unique = true, nullable = false)
    private String dni;

    public boolean hasValidAccess(UsageService usageService) {
        var currentQuota = usageService.getCurrentUsageQuota(this);
        return currentQuota.isPresent();
    }
}
