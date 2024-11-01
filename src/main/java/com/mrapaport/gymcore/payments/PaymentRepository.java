package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.user = :user ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payment> findLastUserPayment(User user);

    @Query("SELECT count(p) FROM Payment p WHERE p.promoAssignment = :promoAssignment")
    Integer countPromoUsages(PromotionAssignment promoAssignment);

    Page<Payment> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Payment> findAllByCreatedAtAfter(LocalDateTime startDate, Pageable pageable);
    Page<Payment> findAllByCreatedAtBefore(LocalDateTime endDate, Pageable pageable);

}
