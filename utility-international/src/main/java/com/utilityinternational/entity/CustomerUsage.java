package com.utilityinternational.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A consumption record: how many units of a particular {@link Resource} a
 * customer used during a billing period. The Calculate Bill use case reads
 * these to work out each line of a bill (units consumed * resource unit price).
 *
 * <p>From the domain model: {@code Resource 1 --- * CustomerUsage} and the
 * various {@code *Units} associations to {@code CustomerUsage}.</p>
 */
@Entity
@Table(name = "customer_usage")
public class CustomerUsage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "units_consumed", nullable = false, precision = 12, scale = 3)
    private BigDecimal unitsConsumed;

    protected CustomerUsage() {
        // Required no-arg constructor for JPA.
    }

    public CustomerUsage(Resource resource, BigDecimal unitsConsumed) {
        this.resource = resource;
        this.unitsConsumed = unitsConsumed;
    }

    /**
     * Line cost for this usage = units consumed * the resource's unit price.
     * Used by the billing service when calculating a {@link MonthlyBill}.
     */
    public BigDecimal lineCost() {
        return unitsConsumed.multiply(resource.getUnitPrice());
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public BigDecimal getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(BigDecimal unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }
}
