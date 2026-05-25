package com.utilityinternational.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A Utility International customer using the self-service application to
 * view, dispute and pay bills.
 *
 * <p>From the domain model: {@code Customer 1 --- * MonthlyBill} and
 * {@code Customer 1 --- * Payment}. Inherits id/audit fields from the
 * {@code @MappedSuperclass} {@link BaseEntity}.</p>
 */
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /** Mobile number for the planned push-notification enhancement. */
    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyBill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    protected Customer() {
        // Required no-arg constructor for JPA.
    }

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addBill(MonthlyBill bill) {
        bills.add(bill);
        bill.setCustomer(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<MonthlyBill> getBills() {
        return bills;
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
