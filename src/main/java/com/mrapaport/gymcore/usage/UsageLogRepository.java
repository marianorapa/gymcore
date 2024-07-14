package com.mrapaport.gymcore.usage;

import com.mrapaport.gymcore.usage.model.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {
}
