package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class Payment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "payment_plan_id")
    private UUID paymentPlanId;

    @Column(name = "amount")
    private Double amount;

    public Payment() {}
}
