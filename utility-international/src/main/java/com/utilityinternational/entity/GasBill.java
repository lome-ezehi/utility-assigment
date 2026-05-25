package com.utilityinternational.entity;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Gas bill — a concrete {@link MonthlyBill}. Stored in the shared single
 * table with discriminator value {@code "GAS"}.
 */
@Entity
@DiscriminatorValue("GAS")
public class GasBill extends MonthlyBill {

    protected GasBill() {
        // Required no-arg constructor for JPA.
    }

    public GasBill(Customer customer, LocalDate periodStart, LocalDate periodEnd) {
        super(customer, periodStart, periodEnd);
    }
}
