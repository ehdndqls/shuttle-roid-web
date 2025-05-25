package com.ehdndqls.shuttle.realtimefleet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RealTimeRepository extends JpaRepository<RealTimeFleet, Long> {

    Optional<RealTimeFleet> findByDailyScheduleId(Long dailyScheduleId);
  //  List<RealTimeFleet> findAll();
}
