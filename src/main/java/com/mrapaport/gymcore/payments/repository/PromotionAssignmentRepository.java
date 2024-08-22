package com.mrapaport.gymcore.payments.repository;

import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionAssignmentRepository extends JpaRepository<PromotionAssignment, UUID> {

}
