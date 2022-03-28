package com.skylife.travel.repository;

import com.skylife.travel.domain.Requirements;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Requirements entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequirementsRepository extends JpaRepository<Requirements, Long> {}
