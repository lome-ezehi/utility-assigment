package com.utilityinternational.entity;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Water bill — a concrete {@link MonthlyBill}. Stored in the shared single
 * table with discriminator value {@code "WATER"}.
 */
@Entity
@DiscriminatorValue("WATER")
public class WaterBill extends MonthlyBill {

    protected WaterBill() {
        // Required no-arg constructor for JPA.
    }

    public WaterBill(Customer customer, LocalDate periodStart, LocalDate periodEnd) {
        super(customer, periodStart, periodEnd);
    }
}
