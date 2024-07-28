package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.PaymentPlan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentPlanService {

    private final PaymentPlanRepository paymentPlanRepository;

    public PaymentPlanService(PaymentPlanRepository repository) {
        this.paymentPlanRepository = repository;
    }

    public List<PaymentPlan> getAllActivePaymentPlans() {
        return paymentPlanRepository.findAllActivePlansUntil(LocalDateTime.now());
    }

    public Optional<PaymentPlan> findById(UUID paymentPlanId) {
        return paymentPlanRepository.findById(paymentPlanId);
    }
}
