package com.mrapaport.gymcore.users.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.payments.PaymentService;
import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.Promotion;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.usage.UsageService;
import com.mrapaport.gymcore.usage.model.UsageQuota;
import com.mrapaport.gymcore.users.UserRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(name = "pin", unique = true, nullable = false)
    private Integer pin;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "dni", unique = true, nullable = false)
    private String dni;

    @ManyToOne
    @JoinColumn(columnDefinition = "payment_plan_id")
    private PaymentPlan paymentPlan;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionAssignment> promotionAssignments;

    public static User saveNew(UserRepository repository, String username, String dni,
            PaymentPlan paymentPlan, String phoneNumber) {
        var user = new User();
        user.dni = dni;
        user.username = username;
        user.paymentPlan = paymentPlan;
        user.pin = generatePin();
        user.phoneNumber = phoneNumber;
        while (repository.findByPin(user.pin).isPresent()) {
            user.pin = generatePin();
        }
        return repository.save(user);
    }

    private static Integer generatePin() {
        return ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
    }

    public static boolean isValid(UserRepository repository, String dni) {
        return repository.findByDni(dni).isEmpty();
    }

    public boolean hasValidAccess(PaymentService paymentService) {
        return paymentService.findLastUserPayment(this).map(Payment::isCurrentlyValid)
                .orElse(false);
    }


}
