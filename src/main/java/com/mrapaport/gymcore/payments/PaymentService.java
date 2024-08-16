package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
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
import java.util.UUID;

@Service
public class PaymentService {

    private PaymentRepository repository;
    private UsageService usageService;
    private UserService userService;

    public PaymentService(PaymentRepository repository, UsageService usageService,
            UserService userService) {
        this.repository = repository;
        this.usageService = usageService;
        this.userService = userService;
    }

    @Transactional
    public void registerPayment(String userDni, double amount, LocalDate expiryDate) {
        var userOpt = userService.findByDni(userDni);
        userOpt.ifPresentOrElse(user -> {
            var payment = Payment.builder().user(user).amount(amount).build();
            repository.save(payment);
            usageService.newQuotaForUser(user, LocalDateTime.of(expiryDate, LocalTime.now()));
        }, () -> {
            throw new UserNotFoundException();
        });
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

}
