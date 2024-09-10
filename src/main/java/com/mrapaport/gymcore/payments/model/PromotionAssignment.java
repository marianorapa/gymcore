package com.mrapaport.gymcore.payments.model;

import java.time.LocalDate;
import java.util.UUID;
import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_promotions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionAssignment extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "usage_exhausted")
    private Boolean usageExhausted;

    public static PromotionAssignment forUserWithPromo(User user, Promotion promo, LocalDate endDate) {
       return new PromotionAssignment(user, promo, LocalDate.now(), endDate, false);
    }

    public boolean isActive() {
        var now = LocalDate.now();
        return usageRemaining() && (startDate.isEqual(now) || startDate.isBefore(now)) && (endDate.isEqual(now) || endDate.isAfter(now));
    }

    private boolean usageRemaining() {
        return usageExhausted == null || usageExhausted.equals(Boolean.FALSE);
    }

    public Double calculateNetCost(Double grossPlanCost) {
        return promotion.calculateNetCost(grossPlanCost);
    }
}
