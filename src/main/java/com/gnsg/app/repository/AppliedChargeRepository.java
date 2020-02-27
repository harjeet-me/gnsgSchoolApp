package com.gnsg.app.repository;

import com.gnsg.app.domain.AppliedCharge;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AppliedCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppliedChargeRepository extends JpaRepository<AppliedCharge, Long> {

}
