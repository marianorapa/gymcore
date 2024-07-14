package com.mrapaport.gymcore.usage;

import com.mrapaport.gymcore.usage.model.UsageQuota;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UsageQuotaRepository extends JpaRepository<UsageQuota, UUID> {

    @Query("select q from UsageQuota q where q.user = :user and q.validUntil >= :validAt")
    Optional<UsageQuota> findQuotaForUser(User user, LocalDateTime validAt);
}
