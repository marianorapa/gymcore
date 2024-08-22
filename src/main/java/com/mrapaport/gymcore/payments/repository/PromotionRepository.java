package com.mrapaport.gymcore.payments.repository;

import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query("SELECT p FROM Promotion p where p.isActive")
    List<Promotion> findAllActivePromos();

}
