package com.utilityinternational.dao;

import java.util.List;
import java.util.Optional;

import com.utilityinternational.entity.MonthlyBill;
import com.utilityinternational.entity.enums.BillStatus;

/**
 * Bill-specific <strong>DAO</strong>. Extends {@link GenericDao} with finder
 * methods needed by the View/Dispute/Pay Bill use cases.
 */
public interface BillDao extends GenericDao<MonthlyBill, Long> {

    /** All bills belonging to a given customer (their workspace view). */
    List<MonthlyBill> findByCustomerId(Long customerId);

    /** All bills currently in a particular lifecycle state. */
    List<MonthlyBill> findByStatus(BillStatus status);

    /**
     * The single most recently generated outstanding bill for a customer —
     * the "most recent bill" the customer selects in the View Bill flow.
     * Returns an {@link Optional} because a customer may have none.
     */
    Optional<MonthlyBill> findMostRecentOutstanding(Long customerId);
}
