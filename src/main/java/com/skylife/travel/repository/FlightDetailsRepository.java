package com.skylife.travel.repository;

import com.skylife.travel.domain.FlightDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlightDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlightDetailsRepository extends JpaRepository<FlightDetails, Long> {}
