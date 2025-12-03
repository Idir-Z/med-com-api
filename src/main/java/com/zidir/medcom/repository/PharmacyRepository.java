package com.zidir.medcom.repository;

import com.zidir.medcom.domain.Pharmacy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pharmacy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {}
