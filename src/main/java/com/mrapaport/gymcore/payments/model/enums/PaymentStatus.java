package com.mrapaport.gymcore.payments.model.enums;

public enum PaymentStatus {
    PENDING("Pendiente"),
    COMPLETED("Pagado"),
    CANCELLED("Cancelado");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String pretty() {
        return this.value;
    }
}
