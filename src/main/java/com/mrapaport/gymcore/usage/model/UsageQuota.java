package com.mrapaport.gymcore.usage.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "usage_quota")
@Builder
@AllArgsConstructor
public class UsageQuota extends BaseEntity {

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    public UsageQuota() {}
}
