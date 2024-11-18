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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime accessUntil;

    @ManyToOne
    @JoinColumn(name = "user_promo_id")
    private PromotionAssignment promoAssignment;

    @Column(name = "payment_method")
    private String paymentMethod;

    public Payment() {}

    public String accessUntilPretty() {
        return accessUntil.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String amountPretty() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    public boolean isCurrentlyValid() {
        return LocalDateTime.now().isBefore(accessUntil);
    }

    public boolean hasPromo() {
       return promoAssignment != null;
    }

    public String getDiscountPercentage() {
        return promoAssignment != null ? promoAssignment.getPromotion().getDiscountPercentage().toString() + "%" : "";
    }

    public LocalDate accessUntilDate() {
        return accessUntil.toLocalDate();
    }
}
