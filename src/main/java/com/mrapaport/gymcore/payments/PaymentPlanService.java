package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.PaymentPlanCost;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.payments.repository.PaymentPlanCostRepository;
import com.mrapaport.gymcore.payments.repository.PaymentPlanRepository;
import com.mrapaport.gymcore.payments.repository.PromotionAssignmentRepository;
import com.mrapaport.gymcore.payments.repository.PromotionRepository;
import com.mrapaport.gymcore.users.model.User;
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
    private final PromotionRepository promoRepository;
    private final PromotionAssignmentRepository promoAssignmentRepo;

    public PaymentPlanService(PaymentPlanRepository paymentPlanRepository,
            PaymentPlanCostRepository paymentPlanCostRepository,
            PromotionRepository promoRepository, PromotionAssignmentRepository promoAssignmentRepo) {
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanCostRepository = paymentPlanCostRepository;
        this.promoRepository = promoRepository;
        this.promoAssignmentRepo = promoAssignmentRepo;
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

    public Object getAllActivePromos() {
        return promoRepository.findAllActivePromos();
    }

    public PromotionAssignment addUserPromotion(User newUser, UUID promotionId) {
        var promo = promoRepository.findById(promotionId).orElseThrow(() -> new EntityNotFoundException());
        var promoAssignment = PromotionAssignment.forUserWithPromo(newUser, promo);
        return promoAssignmentRepo.save(promoAssignment);
    }
}
