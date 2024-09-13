package com.mrapaport.gymcore.payments;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import com.mrapaport.gymcore.payments.model.Payment;

public class PaymentUtils {

    public static Map<String, BigDecimal> buildSummaryByType(Page<Payment> payments) {
        return getAmountByPaymentMethod(payments.getContent());
    }

    public static Map<String, BigDecimal> getAmountByPaymentMethod(List<Payment> payments) {
        return payments.stream()
            .collect(Collectors.groupingBy(
                Payment::getPaymentMethod, 
                Collectors.reducing(BigDecimal.ZERO, 
                                    payment -> BigDecimal.valueOf(payment.getAmount()), 
                                    BigDecimal::add)));
    }

}
