package com.utilityinternational.entity.enums;

/**
 * How a {@code Payment} was made. The Pay Bill use case allows one-off
 * card payments as well as recurring standing orders and direct debits.
 *
 * <p>Part of the dedicated <strong>enum package</strong> and persisted with
 * {@code @Enumerated(EnumType.STRING)}.</p>
 */
public enum PaymentMethod {

    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    STANDING_ORDER("Standing Order"),
    DIRECT_DEBIT("Direct Debit");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
