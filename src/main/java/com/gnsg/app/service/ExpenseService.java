package com.gnsg.app.service;

import com.gnsg.app.domain.Expense;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Expense}.
 */
public interface ExpenseService {

    /**
     * Save a expense.
     *
     * @param expense the entity to save.
     * @return the persisted entity.
     */
    Expense save(Expense expense);

    /**
     * Get all the expenses.
     *
     * @return the list of entities.
     */
    List<Expense> findAll();


    /**
     * Get the "id" expense.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Expense> findOne(Long id);

    /**
     * Delete the "id" expense.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the expense corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Expense> search(String query);
}
