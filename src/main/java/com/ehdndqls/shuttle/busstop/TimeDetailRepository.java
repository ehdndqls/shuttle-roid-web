package com.ehdndqls.shuttle.busstop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeDetailRepository extends JpaRepository<TimeDetail, Integer> {

    @Query("SELECT t.travelTime FROM TimeDetail t " +
            "WHERE t.organizationId = :organizationId " +
            "AND t.startStopId = :startStopId " +
            "AND t.endStopId = :endStopId")
    Optional<Integer> findTravelTime(
            @Param("organizationId") Integer organizationId,
            @Param("startStopId") Integer startStopId,
            @Param("endStopId") Integer endStopId
    );
}
