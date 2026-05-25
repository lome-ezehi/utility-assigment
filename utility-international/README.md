# Utility International — Self-Service Billing (SEN 304)

A Java EE / JPA implementation of the Utility International domain model and
the five use cases (Calculate, View, Dispute, Resolve, Pay Bill), plus the
UML architecture & design document.

## Contents

- `deliverables/utility-international-design.html` — the architecture & design
  deliverable: class, component, deployment and five sequence diagrams (UML),
  the top three technical risks with mitigations, and the assumptions list.
- `src/main/java/...` — the layered implementation.
- `pom.xml`, `src/main/resources/META-INF/persistence.xml` — build & JPA config.

## Build & run

Requires JDK 11+ and Maven. From the project root:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.utilityinternational.DemoApplication
```

`DemoApplication` boots an in-memory H2 database and exercises every use case
end-to-end, printing the resulting DTOs so each technique can be seen working.

(If you prefer, import the project into an IDE as a Maven project and run
`DemoApplication` directly.)

## Required techniques — where each one lives

| Technique | File(s) |
|---|---|
| Data Transfer Object (DTO) | `dto/BillDTO.java` |
| Data Access Object (DAO) | `dao/GenericDao.java`, `dao/BillDao.java`, `dao/BillDaoImpl.java` |
| Optional | `GenericDao.findById`, `BillDao.findMostRecentOutstanding`, consumed in `service/BillService.java` |
| Functional programming | `dto/BillMapper.java` (`Function` lambda, method references), `service/BillService.java` (`reduce`) |
| Dedicated enum package | `entity/enums/` — `UtilityType`, `BillStatus`, `PaymentMethod` |
| `@Enumerated(EnumType.STRING)` | `entity/Resource.java`, `entity/MonthlyBill.java`, `entity/Payment.java` |
| `stream().map().collect(Collectors.toList())` | `dto/BillMapper.toDtoList` |
| `@MappedSuperclass` | `entity/BaseEntity.java` |

## Design notes

- Two JPA inheritance strategies are used deliberately: `@MappedSuperclass`
  (`BaseEntity`) for shared id/version/audit columns, and single-table
  `@Inheritance` for the `MonthlyBill → ElectricityBill/GasBill/WaterBill`
  hierarchy (discriminator column `bill_type`).
- The DAO layer hides the `EntityManager`; the service returns DTOs only, so
  the presentation tier never touches managed entities.
