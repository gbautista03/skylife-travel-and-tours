package com.skylife.travel.repository;

import com.skylife.travel.domain.OHDC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OHDC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OHDCRepository extends JpaRepository<OHDC, Long> {}
