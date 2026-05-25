package com.utilityinternational.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.utilityinternational.entity.enums.PaymentMethod;

/**
 * A payment made by a {@link Customer} against a {@link MonthlyBill} (Pay Bill
 * use case). The actual clearing is performed by the third-party payment
 * system exposed as a JMS queue; this entity records the payment locally.
 *
 * <p>From the domain model: {@code Customer 1 --- * Payment} and
 * {@code Payment * --- 1 MonthlyBill}.</p>
 */
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private MonthlyBill bill;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    /**
     * How the payment was made. Persisted with
     * {@code @Enumerated(EnumType.STRING)} — the "enum.string" requirement.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 20)
    private PaymentMethod method;

    protected Payment() {
        // Required no-arg constructor for JPA.
    }

    public Payment(Customer customer, MonthlyBill bill, BigDecimal amount, PaymentMethod method) {
        this.customer = customer;
        this.bill = bill;
        this.amount = amount;
        this.method = method;
        this.paidAt = LocalDateTime.now();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MonthlyBill getBill() {
        return bill;
    }

    public void setBill(MonthlyBill bill) {
        this.bill = bill;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
}
