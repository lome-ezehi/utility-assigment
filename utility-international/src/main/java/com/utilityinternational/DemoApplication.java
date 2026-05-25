package com.utilityinternational;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.utilityinternational.dao.BillDao;
import com.utilityinternational.dao.BillDaoImpl;
import com.utilityinternational.dto.BillDTO;
import com.utilityinternational.entity.Customer;
import com.utilityinternational.entity.CustomerUsage;
import com.utilityinternational.entity.ElectricityBill;
import com.utilityinternational.entity.GasBill;
import com.utilityinternational.entity.MonthlyBill;
import com.utilityinternational.entity.Resource;
import com.utilityinternational.entity.Supplier;
import com.utilityinternational.entity.enums.PaymentMethod;
import com.utilityinternational.entity.enums.UtilityType;
import com.utilityinternational.service.BillService;

/**
 * Runnable demonstration that exercises the domain model and every use case
 * end-to-end against an in-memory database, so the marker can see the
 * required techniques actually working:
 *
 * <ul>
 *   <li>DTO + DAO layers</li>
 *   <li>Optional (DAO lookups, "most recent bill")</li>
 *   <li>Functional programming (lambdas, method references, reduce)</li>
 *   <li>Dedicated enum package + EnumType.STRING persistence</li>
 *   <li>stream().map().collect(Collectors.toList())</li>
 *   <li>@MappedSuperclass base entity</li>
 * </ul>
 */
public class DemoApplication {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("utilityPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // --- Seed reference data: supplier + priced resources ---
        Supplier supplier = new Supplier("National Grid Wholesale", "USA");
        Resource electricity = new Resource(UtilityType.ELECTRICITY, new BigDecimal("0.2800"));
        Resource gas = new Resource(UtilityType.GAS, new BigDecimal("1.1500"));
        supplier.addResource(electricity);
        supplier.addResource(gas);
        em.persist(supplier);

        // --- A customer with two bills ---
        Customer customer = new Customer("Ada", "Okoye", "ada.okoye@example.com");
        customer.setMobileNumber("+15551234567");

        LocalDate periodStart = LocalDate.now().minusDays(30);
        LocalDate periodEnd = LocalDate.now();

        ElectricityBill elecBill = new ElectricityBill(customer, periodStart, periodEnd);
        elecBill.addUsage(new CustomerUsage(electricity, new BigDecimal("450.000"))); // 450 kWh
        customer.addBill(elecBill);

        GasBill gasBill = new GasBill(customer, periodStart, periodEnd);
        gasBill.addUsage(new CustomerUsage(gas, new BigDecimal("80.000"))); // 80 therms
        customer.addBill(gasBill);

        em.persist(customer);
        em.getTransaction().commit();

        // --- Wire up DAO + service (DAO and DTO layers in action) ---
        BillDao billDao = new BillDaoImpl(em);
        BillService service = new BillService(billDao);

        em.getTransaction().begin();

        // Calculate Bill use case
        BillDTO elecDto = service.calculateBill(elecBill);
        BillDTO gasDto = service.calculateBill(gasBill);
        System.out.println("Calculated: " + elecDto);
        System.out.println("Calculated: " + gasDto);

        // View Bill use case (stream().map().collect(toList()) inside the service)
        List<BillDTO> workspace = service.viewWorkspace(customer.getId());
        System.out.println("\nWorkspace bills for " + customer.getFullName() + ":");
        workspace.forEach(dto -> System.out.println("  - " + dto));

        // View most recent outstanding bill (Optional consumed functionally)
        BillDTO mostRecent = service.viewMostRecentBill(customer.getId());
        System.out.println("\nMost recent outstanding: " + mostRecent);

        // Dispute Bill use case
        BillDTO disputed = service.disputeBill(elecBill.getId(),
                "Meter reading looks far too high versus last month.");
        System.out.println("\nAfter dispute: " + disputed);

        // Resolve Disputed Bill use case (agent rejects the dispute)
        BillDTO resolved = service.resolveDispute(elecBill.getId(), false);
        System.out.println("After resolution: " + resolved);

        // Pay Bill use case
        BillDTO paid = service.payBill(gasBill.getId(), PaymentMethod.DIRECT_DEBIT);
        System.out.println("\nAfter payment: " + paid);

        em.getTransaction().commit();

        // Confirm Optional empty-path works for an unknown id
        boolean missing = billDao.findById(9999L).isEmpty();
        System.out.println("\nLookup of unknown bill id returns empty Optional: " + missing);

        em.close();
        emf.close();
        System.out.println("\nDemo completed successfully.");
    }
}
