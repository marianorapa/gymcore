package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.PaymentPlanCost;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentPlanService {

    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentPlanCostRepository paymentPlanCostRepository;

    public PaymentPlanService(PaymentPlanRepository paymentPlanRepository,
            PaymentPlanCostRepository paymentPlanCostRepository) {
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanCostRepository = paymentPlanCostRepository;
    }

    public List<PaymentPlan> getAllActivePaymentPlans() {
        return paymentPlanRepository.findAllActivePlansUntil(LocalDateTime.now());
    }

    public Optional<PaymentPlan> findById(UUID paymentPlanId) {
        return paymentPlanRepository.findById(paymentPlanId);
    }

    public PaymentPlanCost getCurrentPlanCost(PaymentPlan currentPlan) {
        return paymentPlanCostRepository.findByPaymentPlanActiveAt(currentPlan, LocalDateTime.now())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cost not found for payment plan " + currentPlan.getId()));
    }
}
