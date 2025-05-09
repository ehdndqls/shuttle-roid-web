package com.ehdndqls.shuttle.busstop;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusStopsRepository extends JpaRepository<BusStops, Long> {
    Page<BusStops> findByOrganizationId(Long organizationId, Pageable pageable);
    List<BusStops> findByOrganizationId(Long organizationId);

    @Query("SELECT b FROM BusStops b WHERE " +
            "(:searchText IS NULL OR b.stopName LIKE %:searchText%) AND " +
            "(:organizationId IS NULL OR b.organizationId = :organizationId)")
    List<BusStops> searchBusStops(
            @Param("searchText") String searchText,
            @Param("organizationId") Long organizationId
    );

}
