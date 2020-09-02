package com.gnsg.app.repository;

import com.gnsg.app.domain.Program;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Program entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
}
