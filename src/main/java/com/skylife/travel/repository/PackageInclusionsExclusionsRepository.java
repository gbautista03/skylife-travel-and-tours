package com.skylife.travel.repository;

import com.skylife.travel.domain.PackageInclusionsExclusions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PackageInclusionsExclusions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageInclusionsExclusionsRepository extends JpaRepository<PackageInclusionsExclusions, Long> {}
