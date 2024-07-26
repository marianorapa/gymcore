package com.mrapaport.gymcore.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, UUID> {

    @Query("SELECT p FROM PaymentPlan p where p.validUntil > :dateUntil order by p.planName")
    List<PaymentPlan> findAllActivePlansUntil(LocalDateTime dateUntil);
}
