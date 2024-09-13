package com.mrapaport.gymcore.payments.model;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.data.domain.Page;
import lombok.Data;

public record PaymentsWithSummary (Page<Payment> payments,
Map<String, BigDecimal> amountByPaymentType) {
    
}
