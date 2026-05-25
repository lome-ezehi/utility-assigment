package com.utilityinternational.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.utilityinternational.entity.enums.BillStatus;

/**
 * Abstract base of the bill hierarchy. The domain model shows
 * {@code GasBill}, {@code WaterBill} and {@code ElectricityBill} all
 * generalising to {@code MonthlyBill}.
 *
 * <p>Mapped with JPA <strong>{@code @Inheritance(SINGLE_TABLE)}</strong> and a
 * {@code @DiscriminatorColumn}, so all three concrete bill types share one
 * {@code monthly_bill} table distinguished by a {@code bill_type} column. This
 * is deliberately different from {@link BaseEntity}, which is a
 * {@code @MappedSuperclass}: here we need {@code MonthlyBill} itself to be a
 * queryable entity (a {@code Customer} has a list of {@code MonthlyBill}),
 * which {@code @MappedSuperclass} could not provide.</p>
 */
@Entity
@Table(name = "monthly_bill")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "bill_type", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class MonthlyBill extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "billing_period_start", nullable = false)
    private LocalDate billingPeriodStart;

    @Column(name = "billing_period_end", nullable = false)
    private LocalDate billingPeriodEnd;

    @Column(name = "generated_on", nullable = false)
    private LocalDate generatedOn;

    @Column(name = "amount_due", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountDue = BigDecimal.ZERO;

    /**
     * Current lifecycle state. Persisted with
     * {@code @Enumerated(EnumType.STRING)} — the "enum.string" requirement.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BillStatus status = BillStatus.OUTSTANDING;

    /** Free-text context supplied by the customer when disputing (Dispute Bill use case). */
    @Column(name = "dispute_note", length = 2000)
    private String disputeNote;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bill_id")
    private List<CustomerUsage> usages = new ArrayList<>();

    protected MonthlyBill() {
        // Required no-arg constructor for JPA.
    }

    protected MonthlyBill(Customer customer, LocalDate periodStart, LocalDate periodEnd) {
        this.customer = customer;
        this.billingPeriodStart = periodStart;
        this.billingPeriodEnd = periodEnd;
        this.generatedOn = LocalDate.now();
    }

    /**
     * The deadline (30 days after generation) by which a customer may dispute
     * this bill, per the business rules.
     */
    public LocalDate disputeDeadline() {
        return generatedOn.plusDays(30);
    }

    public boolean isDisputable(LocalDate asOf) {
        return status == BillStatus.OUTSTANDING && !asOf.isAfter(disputeDeadline());
    }

    public void addUsage(CustomerUsage usage) {
        usages.add(usage);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public LocalDate getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public LocalDate getGeneratedOn() {
        return generatedOn;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public String getDisputeNote() {
        return disputeNote;
    }

    public void setDisputeNote(String disputeNote) {
        this.disputeNote = disputeNote;
    }

    public List<CustomerUsage> getUsages() {
        return usages;
    }
}
