package com.utilityinternational.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A wholesale supplier of one or more {@link Resource}s (electricity, gas or
 * water) that Utility International then bundles and resells to customers.
 *
 * <p>From the domain model: {@code Supplier 1 --- * Resource}. Inherits its
 * identifier and audit fields from the {@code @MappedSuperclass}
 * {@link BaseEntity}.</p>
 */
@Entity
@Table(name = "supplier")
public class Supplier extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();

    protected Supplier() {
        // Required no-arg constructor for JPA.
    }

    public Supplier(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
        resource.setSupplier(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
