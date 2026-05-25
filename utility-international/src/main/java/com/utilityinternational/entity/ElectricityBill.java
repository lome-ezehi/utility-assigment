package com.utilityinternational.entity;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Electricity bill — a concrete {@link MonthlyBill}. Stored in the shared
 * single table with discriminator value {@code "ELECTRICITY"}.
 */
@Entity
@DiscriminatorValue("ELECTRICITY")
public class ElectricityBill extends MonthlyBill {

    protected ElectricityBill() {
        // Required no-arg constructor for JPA.
    }

    public ElectricityBill(Customer customer, LocalDate periodStart, LocalDate periodEnd) {
        super(customer, periodStart, periodEnd);
    }
}
