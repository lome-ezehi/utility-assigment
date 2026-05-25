package com.utilityinternational.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <strong>Data Transfer Object (DTO)</strong> for a monthly bill.
 *
 * <p>This is a flat, serialisable carrier of just the data the self-service
 * web/presentation tier needs to render a bill in the customer's workspace.
 * It deliberately does NOT expose the JPA {@code MonthlyBill} entity, so that
 * the persistence layer (lazy associations, the inheritance hierarchy, the
 * managed entity lifecycle) is fully decoupled from the client. Entities are
 * converted to DTOs in the service layer using a stream-map-collect pipeline.</p>
 */
public class BillDTO {

    private final Long id;
    private final String customerName;
    private final String utilityType;   // String form of the UtilityType / bill type
    private final String status;         // String form of the BillStatus enum
    private final LocalDate generatedOn;
    private final LocalDate disputeDeadline;
    private final BigDecimal amountDue;

    public BillDTO(Long id, String customerName, String utilityType, String status,
                   LocalDate generatedOn, LocalDate disputeDeadline, BigDecimal amountDue) {
        this.id = id;
        this.customerName = customerName;
        this.utilityType = utilityType;
        this.status = status;
        this.generatedOn = generatedOn;
        this.disputeDeadline = disputeDeadline;
        this.amountDue = amountDue;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getGeneratedOn() {
        return generatedOn;
    }

    public LocalDate getDisputeDeadline() {
        return disputeDeadline;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    @Override
    public String toString() {
        return "BillDTO{id=" + id
                + ", customer='" + customerName + '\''
                + ", utility='" + utilityType + '\''
                + ", status='" + status + '\''
                + ", amountDue=" + amountDue
                + ", generatedOn=" + generatedOn
                + ", disputeDeadline=" + disputeDeadline + '}';
    }
}
