package com.ehdndqls.shuttle.busstop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface EstimatedTimeRepository extends JpaRepository<EstimatedTime, EstimatedTimeId> {

    @Query("SELECT e FROM EstimatedTime e WHERE e.id.departureStop = :dep AND e.id.arrivalStop = :arr AND e.duration = :time")
    Optional<EstimatedTime> findByDepartureArrivalAndTime(
            @Param("dep") Integer departureStop,
            @Param("arr") Integer arrivalStop,
            @Param("time") Integer time
    );
}
