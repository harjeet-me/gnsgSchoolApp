package com.gnsg.app.repository;

import com.gnsg.app.domain.Expense;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Expense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
