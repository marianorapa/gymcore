package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PaymentBuilder;
import com.mrapaport.gymcore.payments.model.PaymentPlanCost;
import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import com.mrapaport.gymcore.usage.UsageService;
import com.mrapaport.gymcore.users.UserService;
import com.mrapaport.gymcore.users.exception.UserNotFoundException;
import com.mrapaport.gymcore.users.model.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public void createPayment(UUID userId, double amount) {
        var user = userService.findById(userId);
        var lastPayment = repository.findLastUserPayment(user).orElse(null);
        var currentPlanCost = paymentPlanService.getCurrentPlanCost(user.getPaymentPlan());
        var newPayment = PaymentBuilder.builder().forUser(user).withAmount(amount)
                .withCurrentPlanCost(currentPlanCost).withLastPayment(lastPayment).build();

        repository.save(newPayment);
    }

    public Optional<Payment> findLastUserPayment(User user) {
        return repository.findLastUserPayment(user);
    }

}
