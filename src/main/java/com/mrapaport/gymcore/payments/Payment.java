package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "payment_plan_id")
    private UUID paymentPlanId;

    @Column(name = "amount")
    private Double amount;
}
