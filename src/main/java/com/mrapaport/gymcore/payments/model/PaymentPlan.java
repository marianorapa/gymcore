package com.mrapaport.gymcore.payments.model;

import com.mrapaport.gymcore.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_plans")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPlan extends BaseEntity {

    @Column(name = "plan_name", length = 255)
    private String planName;

    @Column(name = "plan_description", length = 255)
    private String planDescription;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;
}
