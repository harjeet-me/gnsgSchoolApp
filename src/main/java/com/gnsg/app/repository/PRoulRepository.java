package com.gnsg.app.repository;

import com.gnsg.app.domain.PRoul;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PRoul entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PRoulRepository extends JpaRepository<PRoul, Long> {
}
