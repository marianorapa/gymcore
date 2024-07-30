package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.PaymentPlanCost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PaymentPlanCostRepository extends JpaRepository<PaymentPlanCost, UUID> {


    @Query("SELECT p from PaymentPlanCost p where p.paymentPlan = :plan and p.validFrom <= :timestamp and p.validUntil >= :timestamp")
    Optional<PaymentPlanCost> findByPaymentPlanActiveAt(PaymentPlan plan, LocalDateTime timestamp);
}
