package com.mrapaport.gymcore.usage.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "usage_quota")
@Builder
@AllArgsConstructor
@Data
public class UsageQuota extends BaseEntity {

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    public UsageQuota() {}

    public String validUntilPretty() {
        return validUntil.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
