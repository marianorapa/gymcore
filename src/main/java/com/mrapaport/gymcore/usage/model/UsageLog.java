package com.mrapaport.gymcore.usage.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usage_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class UsageLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "valid_quota", nullable = false)
    private boolean validQuota;

    public static UsageLog forUser(User user, Boolean validAccess) {
        var log = new UsageLog();
        log.user = user;
        log.validQuota = validAccess;
        return log;
    }
}
