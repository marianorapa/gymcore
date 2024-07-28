package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
