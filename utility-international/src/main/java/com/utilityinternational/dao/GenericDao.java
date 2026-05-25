package com.utilityinternational.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic <strong>Data Access Object (DAO)</strong> contract.
 *
 * <p>The DAO pattern encapsulates all persistence logic behind a plain Java
 * interface, so the service layer never touches the {@code EntityManager}
 * directly. Lookups that may not find a row return an
 * <strong>{@link java.util.Optional}</strong> rather than {@code null},
 * forcing callers to handle the "not found" case explicitly.</p>
 *
 * @param <T>  the entity type
 * @param <ID> the entity's identifier type
 */
public interface GenericDao<T, ID> {

    T save(T entity);

    /** Returns the entity wrapped in an {@link Optional}, empty if not found. */
    Optional<T> findById(ID id);

    List<T> findAll();

    void delete(T entity);
}
