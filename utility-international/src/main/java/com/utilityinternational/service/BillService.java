package com.utilityinternational.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.utilityinternational.dao.BillDao;
import com.utilityinternational.dto.BillDTO;
import com.utilityinternational.dto.BillMapper;
import com.utilityinternational.entity.MonthlyBill;
import com.utilityinternational.entity.Payment;
import com.utilityinternational.entity.enums.BillStatus;
import com.utilityinternational.entity.enums.PaymentMethod;
import com.utilityinternational.exception.BillNotFoundException;

/**
 * Application service implementing the bill use cases (View, Dispute, Resolve,
 * Pay) on top of the {@link BillDao}.
 *
 * <p>This layer is where the required techniques come together: it returns
 * {@link BillDTO}s (never raw entities) to its callers, builds those DTO lists
 * with the {@code stream().map().collect(Collectors.toList())} pipeline via
 * {@link BillMapper}, and consumes {@link java.util.Optional} results from the
 * DAO in a functional style ({@code map}, {@code orElseThrow}).</p>
 */
public class BillService {

    private final BillDao billDao;

    public BillService(BillDao billDao) {
        this.billDao = billDao;
    }

    /**
     * Calculate Bill use case (core arithmetic): sum the line cost of every
     * usage on the bill, store it as the amount due and persist.
     */
    public BillDTO calculateBill(MonthlyBill bill) {
        BigDecimal total = bill.getUsages().stream()
                .map(usage -> usage.lineCost())               // functional: entity -> line cost
                .reduce(BigDecimal.ZERO, BigDecimal::add);     // functional reduction
        bill.setAmountDue(total);
        bill.setStatus(BillStatus.OUTSTANDING);
        return BillMapper.toDto(billDao.save(bill));
    }

    /**
     * View Bill use case: list every bill in a customer's workspace as DTOs.
     * Uses the required stream-map-collect pipeline (inside {@link BillMapper}).
     */
    public List<BillDTO> viewWorkspace(Long customerId) {
        List<MonthlyBill> bills = billDao.findByCustomerId(customerId);
        return BillMapper.toDtoList(bills);
    }

    /**
     * View Bill use case: the single most recent outstanding bill.
     * Demonstrates functional consumption of an {@link java.util.Optional} —
     * {@code map} to a DTO, or throw if absent.
     */
    public BillDTO viewMostRecentBill(Long customerId) {
        return billDao.findMostRecentOutstanding(customerId)
                .map(BillMapper::toDto)
                .orElseThrow(() -> new BillNotFoundException(
                        "No outstanding bill for customer " + customerId));
    }

    /**
     * Dispute Bill use case: customer rejects a bill within 30 days and
     * supplies supporting text, which is forwarded to a call centre agent.
     */
    public BillDTO disputeBill(Long billId, String supportingContext) {
        MonthlyBill bill = billDao.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill " + billId + " not found"));

        if (!bill.isDisputable(LocalDate.now())) {
            throw new IllegalStateException(
                    "Bill " + billId + " can no longer be disputed (30-day window elapsed).");
        }
        bill.setStatus(BillStatus.DISPUTED);
        bill.setDisputeNote(supportingContext);
        return BillMapper.toDto(billDao.save(bill));
    }

    /**
     * Resolve Disputed Bill use case: the call centre agent either upholds the
     * dispute (RESOLVED) or rejects it and returns the bill for payment.
     */
    public BillDTO resolveDispute(Long billId, boolean disputeUpheld) {
        MonthlyBill bill = billDao.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill " + billId + " not found"));

        bill.setStatus(disputeUpheld ? BillStatus.RESOLVED : BillStatus.REJECTED_DISPUTE);
        return BillMapper.toDto(billDao.save(bill));
    }

    /**
     * Pay Bill use case: record a full payment against an outstanding bill and
     * mark it paid. (Clearing is delegated to the third-party JMS payment
     * system in the real deployment; here we record the local Payment.)
     */
    public BillDTO payBill(Long billId, PaymentMethod method) {
        MonthlyBill bill = billDao.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill " + billId + " not found"));

        Payment payment = new Payment(
                bill.getCustomer(), bill, bill.getAmountDue(), method);
        bill.getCustomer().getPayments().add(payment);
        bill.setStatus(BillStatus.PAID);
        return BillMapper.toDto(billDao.save(bill));
    }
}
