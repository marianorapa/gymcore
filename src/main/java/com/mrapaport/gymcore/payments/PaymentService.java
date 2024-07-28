package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.usage.UsageService;
import com.mrapaport.gymcore.users.UserService;
import com.mrapaport.gymcore.users.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PaymentService {

    private PaymentRepository repository;
    private UsageService usageService;
    private UserService userService;

    public PaymentService(PaymentRepository repository, UsageService usageService, UserService userService) {
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
        }, () -> { throw new UserNotFoundException(); });
    }
}
