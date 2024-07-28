package com.mrapaport.gymcore.payments.model;

import com.mrapaport.gymcore.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_plan_cost")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPlanCost extends BaseEntity {

    @ManyToOne
    @JoinColumn(columnDefinition = "payment_plan_id")
    private PaymentPlan paymentPlan;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;
}
