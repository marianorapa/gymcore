package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.user = :user ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payment> findLastUserPayment(User user);
}
