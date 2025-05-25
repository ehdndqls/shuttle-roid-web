package com.ehdndqls.shuttle.busstop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface EstimatedTimeRepository extends JpaRepository<EstimatedTime, EstimatedTimeId> {

    Optional<EstimatedTime> findById_DepartureStopAndId_ArrivalStop(BusStopId departureStop, BusStopId arrivalStop);

}
