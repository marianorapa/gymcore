package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PaymentBuilder;
import com.mrapaport.gymcore.payments.model.PaymentPlanCost;
import com.mrapaport.gymcore.payments.model.PaymentsWithSummary;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import com.mrapaport.gymcore.usage.UsageService;
import com.mrapaport.gymcore.users.UserService;
import com.mrapaport.gymcore.users.exception.UserNotFoundException;
import com.mrapaport.gymcore.users.model.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.flywaydb.core.api.configuration.Configuration;
import org.hibernate.mapping.Array;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final UserService userService;
    private final PaymentPlanService paymentPlanService;

    public PaymentService(PaymentRepository repository,
            UserService userService, PaymentPlanService paymentPlanService) {
        this.repository = repository;
        this.userService = userService;
        this.paymentPlanService = paymentPlanService;
    }

    public Page<Payment> findByUserId(UUID userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    public Payment updatePaymentStatus(UUID paymentId, PaymentStatus status) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        payment.setStatus(status);
        return repository.save(payment);
    }

    public Payment findById(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User findUserByPaymentId(UUID paymentId) {
        return this.findById(paymentId).getUser();
    }

    public void createPayment(UUID userId, double amount, String paymentMethod) {
        var user = userService.findById(userId);
        var lastPayment = repository.findLastUserPayment(user).orElse(null);
        var currentPlanCost = paymentPlanService.getCurrentPlanCost(user.getPaymentPlan());
        var newPayment = PaymentBuilder.builder().forUser(user).withAmount(amount)
                .withCurrentPlanCost(currentPlanCost).withLastPayment(lastPayment).withPaymentMethod(paymentMethod).build();

        repository.save(newPayment);

        if (newPayment.hasPromo()) {
            PromotionAssignment promoAssignment = newPayment.getPromoAssignment();
            var promoUsages = repository.countPromoUsages(promoAssignment);
            if (promoUsages >= promoAssignment.getPromotion().getMaxUsageCount()) {
                paymentPlanService.promotionExhausted(promoAssignment);
            }
        }
    }

    public Optional<Payment> findLastUserPayment(User user) {
        return repository.findLastUserPayment(user);
    }

    public PaymentsWithSummary getPaymentsAndSummary(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Payment> payments;
        if (startDate != null && endDate != null) {
            payments = repository.findAllByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), pageable);
        } else if (startDate != null) {
            payments = repository.findAllByCreatedAtAfter(startDate.atStartOfDay(), pageable);
        } else if (endDate != null) {
            payments = repository.findAllByCreatedAtBefore(endDate.atTime(23, 59, 59), pageable);
        } else {
            payments = repository.findAll(pageable);
        }

        return new PaymentsWithSummary(payments, PaymentUtils.buildSummaryByType(payments));
    }
    
}
