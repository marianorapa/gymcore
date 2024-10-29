package com.mrapaport.gymcore.payments.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.mrapaport.gymcore.users.model.User;

public class PaymentBuilder {

    private User user;
    private double paymentAmount;
    private int purchasedDays;
    private Payment lastPayment;
    private PaymentPlanCost currentPlanCost;
    private LocalDateTime accessUntil;
    private PromotionAssignment promotion;
    private String paymentMethod;
    
    public PaymentBuilder forUser(User user) {
        this.user = user;
        return this;
    }

    public PaymentBuilder withAmount(double amount) {
        this.paymentAmount = amount;
        return this;
    }

    public Payment build() {
        var payment = new Payment();
        payment.setUser(user);
        payment.setAmount(paymentAmount);
        payment.setPaymentPlanCost(currentPlanCost);
        payment.setPaymentMethod(paymentMethod);
        calculatePurchasedDays();
        calculateAccessDuration();
        payment.setAccessUntil(accessUntil);
        payment.setPromoAssignment(promotion);
        return payment;
    }

    private void calculateAccessDuration() {
        var startingDate = determineStartingDate();
        accessUntil = startingDate.plusDays(purchasedDays);
    }

    private LocalDateTime determineStartingDate() {
        if (lastPayment != null) {
            return startingDateBasedFromLastPayment();
        }
        return LocalDateTime.now();
    }

    private LocalDateTime startingDateBasedFromLastPayment() {
        if (lastPaymentStillAllowsAccess() || lastPaymentRecentlyExpired()) {
            return lastPayment.getAccessUntil();
        }
        else {
            return LocalDateTime.now();
        }
    }

    private boolean lastPaymentRecentlyExpired() {
        return lastPayment.getAccessUntil().isAfter(LocalDateTime.now().minus(10, ChronoUnit.DAYS));
    }

    private boolean lastPaymentStillAllowsAccess() {
        return lastPayment.getAccessUntil().isAfter(LocalDateTime.now());
    }

    private void calculatePurchasedDays() {
        if (currentPlanCost.getAmount() <= 0) {
            purchasedDays = 36500; // 100 years
        }
        else {
            var grossPlanCost = currentPlanCost.getAmount();
            var netPlanCost = user.getActivePromotion()
                .map(promo -> {
                    this.promotion = promo;
                    return promo.calculateNetCost(grossPlanCost);
                })
                .orElse(grossPlanCost);
            var dayCost = netPlanCost / 31;
            purchasedDays = Math.toIntExact(Math.round(paymentAmount / dayCost));
        }
    }

    public PaymentBuilder withLastPayment(Payment lastPayment) {
        this.lastPayment = lastPayment;
        return this;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public PaymentBuilder withCurrentPlanCost(PaymentPlanCost currentPlanCost) {
        this.currentPlanCost = currentPlanCost;
        return this;
    }

    public PaymentBuilder withPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
}
