package com.utilityinternational.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.utilityinternational.entity.enums.UtilityType;

/**
 * A priced utility resource offered by a {@link Supplier} — e.g. electricity
 * at a given price-per-kWh. {@link CustomerUsage} records consumption against
 * a {@code Resource}, and the billing system multiplies units consumed by the
 * resource's unit price when it calculates a bill.
 *
 * <p>From the domain model: {@code Supplier 1 --- * Resource} and
 * {@code Resource 1 --- * CustomerUsage}.</p>
 */
@Entity
@Table(name = "resource")
public class Resource extends BaseEntity {

    /**
     * Which utility this resource provides. Persisted with
     * <strong>{@code @Enumerated(EnumType.STRING)}</strong> so the column
     * stores the readable enum name (e.g. {@code "ELECTRICITY"}) rather than
     * an ordinal — this is the "enum.string" requirement.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "utility_type", nullable = false, length = 20)
    private UtilityType utilityType;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 4)
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    protected Resource() {
        // Required no-arg constructor for JPA.
    }

    public Resource(UtilityType utilityType, BigDecimal unitPrice) {
        this.utilityType = utilityType;
        this.unitPrice = unitPrice;
    }

    public UtilityType getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(UtilityType utilityType) {
        this.utilityType = utilityType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
