package com.utilityinternational.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

/**
 * Abstract base class for every persistent entity in the Utility
 * International domain model.
 *
 * <p>Annotated with <strong>{@code @MappedSuperclass}</strong>: it is NOT an
 * entity in its own right and has no table of its own. Instead, JPA copies its
 * mapped fields (the primary key, the optimistic-lock version and the audit
 * timestamps) down into the table of each concrete subclass that extends it.
 * This removes the boilerplate of redeclaring an id and audit columns on
 * {@code Customer}, {@code Payment}, {@code Supplier}, {@code Resource},
 * {@code CustomerUsage} and the {@code MonthlyBill} hierarchy.</p>
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /** Optimistic-locking version, important under concurrent self-service access. */
    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
