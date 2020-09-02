package com.gnsg.app.repository;

import com.gnsg.app.domain.ASPath;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ASPath entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ASPathRepository extends JpaRepository<ASPath, Long> {
}
