package com.mrapaport.gymcore.usage;

import com.mrapaport.gymcore.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usage_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class UsageLog extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "valid_quota", nullable = false)
    private boolean validQuota;
}
