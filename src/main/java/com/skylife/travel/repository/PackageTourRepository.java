package com.skylife.travel.repository;

import com.skylife.travel.domain.PackageTour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PackageTour entity.
 */
@Repository
public interface PackageTourRepository extends PackageTourRepositoryWithBagRelationships, JpaRepository<PackageTour, Long> {
    default Optional<PackageTour> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<PackageTour> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<PackageTour> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct packageTour from PackageTour packageTour left join fetch packageTour.inclusionExclusion left join fetch packageTour.requirements left join fetch packageTour.ohdc",
        countQuery = "select count(distinct packageTour) from PackageTour packageTour"
    )
    Page<PackageTour> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct packageTour from PackageTour packageTour left join fetch packageTour.inclusionExclusion left join fetch packageTour.requirements left join fetch packageTour.ohdc"
    )
    List<PackageTour> findAllWithToOneRelationships();

    @Query(
        "select packageTour from PackageTour packageTour left join fetch packageTour.inclusionExclusion left join fetch packageTour.requirements left join fetch packageTour.ohdc where packageTour.id =:id"
    )
    Optional<PackageTour> findOneWithToOneRelationships(@Param("id") Long id);
}
