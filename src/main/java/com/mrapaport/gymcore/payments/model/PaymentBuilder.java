package com.mrapaport.gymcore.payments.model;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import com.mrapaport.gymcore.users.model.User;

public class PaymentBuilder {

    private User user;
    private double paymentAmount;
    private int purchasedDays;
    private Payment lastPayment;
    private PaymentPlanCost currentPlanCost;
    private LocalDateTime accessUntil;

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
        calculatePurchasedDays();
        calculateAccessDuration();
        payment.setAccessUntil(accessUntil);
        return payment;
    }

    private void calculateAccessDuration() {
        var startingDate =
                lastPayment != null && lastPayment.getAccessUntil().isAfter(LocalDateTime.now())
                        ? lastPayment.getAccessUntil()
                        : LocalDateTime.now();
        accessUntil = startingDate.plusDays(purchasedDays);
    }

    private void calculatePurchasedDays() {
        var planCost = currentPlanCost.getAmount();
        var dayCost = planCost / 31;
        purchasedDays = Math.toIntExact(Math.round(paymentAmount / dayCost));
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

}
