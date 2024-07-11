package com.mrapaport.gymcore.usage;

import org.springframework.stereotype.Service;

@Service
public class UsageService {
    public boolean determineAccess(String userId) {
        return userId.equals("1234");
    }
}
