package com.mrapaport.gymcore.usage;

import com.mrapaport.gymcore.usage.model.UsageLog;
import com.mrapaport.gymcore.usage.model.UsageQuota;
import com.mrapaport.gymcore.users.UserService;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsageService {

    UsageQuotaRepository repository;
    UsageLogRepository usageLogRepository;
    UserService userService;

    public UsageService(UsageQuotaRepository repository, UsageLogRepository usageLogRepository, UserService userService) {
        this.repository = repository;
        this.usageLogRepository = usageLogRepository;
        this.userService = userService;
    }

    public boolean determineAccess(String userId) {
        var optUser = userService.findByUserId(userId);
        return optUser.map(user -> {
            var valid = user.hasValidAccess(this);
            usageLogRepository.save(UsageLog.forUser(optUser.get(), valid));
            return valid;
        }).orElse(false);
    }

    public Optional<UsageQuota> getCurrentUsageQuota(User user) {
        return repository.findQuotaForUser(user, LocalDateTime.now());
    }
}
