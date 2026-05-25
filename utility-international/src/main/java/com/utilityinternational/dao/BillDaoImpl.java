package com.utilityinternational.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.utilityinternational.entity.MonthlyBill;
import com.utilityinternational.entity.enums.BillStatus;

/**
 * JPA-backed implementation of {@link BillDao}.
 *
 * <p>Demonstrates the DAO pattern over an {@link EntityManager}, returning
 * {@link Optional} for single-result lookups and using functional/stream
 * style where it reads naturally (e.g. selecting the most recent outstanding
 * bill via {@code stream().max(Comparator...)}).</p>
 *
 * <p>Annotated as a CDI bean so it can be injected into the service layer in a
 * Java EE container; it works equally well constructed manually in a unit
 * test by supplying an {@code EntityManager}.</p>
 */
public class BillDaoImpl implements BillDao {

    @PersistenceContext(unitName = "utilityPU")
    private EntityManager em;

    public BillDaoImpl() {
        // Default constructor for container injection.
    }

    public BillDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public MonthlyBill save(MonthlyBill entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            return entity;
        }
        return em.merge(entity);
    }

    @Override
    public Optional<MonthlyBill> findById(Long id) {
        // EntityManager.find may return null -> wrap in Optional.
        return Optional.ofNullable(em.find(MonthlyBill.class, id));
    }

    @Override
    public List<MonthlyBill> findAll() {
        return em.createQuery("SELECT b FROM MonthlyBill b", MonthlyBill.class)
                .getResultList();
    }

    @Override
    public void delete(MonthlyBill entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public List<MonthlyBill> findByCustomerId(Long customerId) {
        TypedQuery<MonthlyBill> query = em.createQuery(
                "SELECT b FROM MonthlyBill b WHERE b.customer.id = :cid", MonthlyBill.class);
        query.setParameter("cid", customerId);
        return query.getResultList();
    }

    @Override
    public List<MonthlyBill> findByStatus(BillStatus status) {
        TypedQuery<MonthlyBill> query = em.createQuery(
                "SELECT b FROM MonthlyBill b WHERE b.status = :status", MonthlyBill.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public Optional<MonthlyBill> findMostRecentOutstanding(Long customerId) {
        // Functional pipeline: filter outstanding bills, then pick the latest
        // by generation date. max() naturally yields an Optional.
        return findByCustomerId(customerId).stream()
                .filter(bill -> bill.getStatus() == BillStatus.OUTSTANDING)
                .max(Comparator.comparing(MonthlyBill::getGeneratedOn));
    }
}
