package com.skylife.travel.repository;

import com.skylife.travel.domain.PackageTour;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PackageTourRepositoryWithBagRelationshipsImpl implements PackageTourRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<PackageTour> fetchBagRelationships(Optional<PackageTour> packageTour) {
        return packageTour.map(this::fetchPassengers).map(this::fetchFlightDetails);
    }

    @Override
    public Page<PackageTour> fetchBagRelationships(Page<PackageTour> packageTours) {
        return new PageImpl<>(
            fetchBagRelationships(packageTours.getContent()),
            packageTours.getPageable(),
            packageTours.getTotalElements()
        );
    }

    @Override
    public List<PackageTour> fetchBagRelationships(List<PackageTour> packageTours) {
        return Optional.of(packageTours).map(this::fetchPassengers).map(this::fetchFlightDetails).get();
    }

    PackageTour fetchPassengers(PackageTour result) {
        return entityManager
            .createQuery(
                "select packageTour from PackageTour packageTour left join fetch packageTour.passengers where packageTour is :packageTour",
                PackageTour.class
            )
            .setParameter("packageTour", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PackageTour> fetchPassengers(List<PackageTour> packageTours) {
        return entityManager
            .createQuery(
                "select distinct packageTour from PackageTour packageTour left join fetch packageTour.passengers where packageTour in :packageTours",
                PackageTour.class
            )
            .setParameter("packageTours", packageTours)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    PackageTour fetchFlightDetails(PackageTour result) {
        return entityManager
            .createQuery(
                "select packageTour from PackageTour packageTour left join fetch packageTour.flightDetails where packageTour is :packageTour",
                PackageTour.class
            )
            .setParameter("packageTour", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PackageTour> fetchFlightDetails(List<PackageTour> packageTours) {
        return entityManager
            .createQuery(
                "select distinct packageTour from PackageTour packageTour left join fetch packageTour.flightDetails where packageTour in :packageTours",
                PackageTour.class
            )
            .setParameter("packageTours", packageTours)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
