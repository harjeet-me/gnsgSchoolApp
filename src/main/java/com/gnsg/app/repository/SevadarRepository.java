package com.gnsg.app.repository;

import com.gnsg.app.domain.Sevadar;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Sevadar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SevadarRepository extends JpaRepository<Sevadar, Long> {
}
