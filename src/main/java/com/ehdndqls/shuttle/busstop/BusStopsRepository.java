package com.ehdndqls.shuttle.busstop;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusStopsRepository extends JpaRepository<BusStops, BusStopId> {

    Page<BusStops> findByOrganizationId(Integer organizationId, Pageable pageable);
    List<BusStops> findByOrganizationId(Integer organizationId);
    Optional<BusStops> findById(BusStopId id);

    @Query("SELECT b FROM BusStops b WHERE " +
            "(:searchText IS NULL OR b.stopName LIKE %:searchText%) AND " +
            "(:organizationId IS NULL OR b.id.organizationId = :organizationId)")
    List<BusStops> searchBusStops(
            @Param("searchText") String searchText,
            @Param("organizationId") Integer organizationId
    );

    // 일반 정류소용 (via 아님): stopId < 9000
    @Query("SELECT MAX(b.id.stopId) FROM BusStops b " +
            "WHERE b.id.organizationId = :organizationId AND b.id.stopId < 9000")
    Optional<Integer> findMaxNormalStopIdByOrganizationId(@Param("organizationId") Integer organizationId);

    // 경유 정류소용 (via): stopId >= 9000
    @Query("SELECT MAX(b.id.stopId) FROM BusStops b " +
            "WHERE b.id.organizationId = :organizationId AND b.id.stopId >= 9000")
    Optional<Integer> findMaxViaStopIdByOrganizationId(@Param("organizationId") Integer organizationId);

    List<BusStops> findAllByOrganizationIdAndStopIds(Integer organizationId, List<Integer> stopIds);

}
