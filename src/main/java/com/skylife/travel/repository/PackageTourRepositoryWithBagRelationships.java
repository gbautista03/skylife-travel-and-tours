package com.skylife.travel.repository;

import com.skylife.travel.domain.PackageTour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PackageTourRepositoryWithBagRelationships {
    Optional<PackageTour> fetchBagRelationships(Optional<PackageTour> packageTour);

    List<PackageTour> fetchBagRelationships(List<PackageTour> packageTours);

    Page<PackageTour> fetchBagRelationships(Page<PackageTour> packageTours);
}
