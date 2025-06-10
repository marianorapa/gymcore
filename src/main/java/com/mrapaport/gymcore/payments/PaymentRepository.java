package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId AND (p.status != :status OR p.status IS NULL)")
    Page<Payment> findByUserIdAndStatusNotOrNull(UUID userId, PaymentStatus status, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.user = :user AND (p.status != :status OR p.status IS NULL) ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payment> findLastUserPaymentStatusNotOrNull(User user, PaymentStatus status);

    @Query("SELECT count(p) FROM Payment p WHERE p.promoAssignment = :promoAssignment")
    Integer countPromoUsages(PromotionAssignment promoAssignment);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate AND (p.status != :paymentStatus OR p.status IS NULL)")
    Page<Payment> findAllByCreatedAtBetweenAndStatusNotOrNull(LocalDateTime startDate, LocalDateTime endDate, PaymentStatus paymentStatus, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.createdAt > :startDate AND (p.status != :paymentStatus OR p.status IS NULL)")
    Page<Payment> findAllByCreatedAtAfterAndStatusNotOrNull(LocalDateTime startDate, PaymentStatus paymentStatus, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt < :endDate AND (p.status != :paymentStatus OR p.status IS NULL)")
    Page<Payment> findAllByCreatedAtBeforeAndStatusNotOrNull(LocalDateTime endDate, PaymentStatus paymentStatus, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE (p.status != :paymentStatus OR p.status IS NULL)")
    Page<Payment> findAllByStatusNotOrNull(Pageable pageable, PaymentStatus paymentStatus);

}
