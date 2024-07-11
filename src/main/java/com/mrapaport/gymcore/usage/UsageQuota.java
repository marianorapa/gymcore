package com.mrapaport.gymcore.usage;

import com.mrapaport.gymcore.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usage_quota")
@Data
@EqualsAndHashCode(callSuper = true)
public class UsageQuota extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;
}
