package com.mrapaport.gymcore.payments.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import com.mrapaport.gymcore.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

    @ManyToOne
    @JoinColumn(name = "payment_plan_cost_id")
    private PaymentPlanCost paymentPlanCost;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "access_until")
    private LocalDateTime accessUntil;

    public Payment() {}

    public String accessUntilPretty() {
        return accessUntil.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String amountPretty() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
