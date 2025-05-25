package com.ehdndqls.shuttle.schedules;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailySchedulesRepository extends JpaRepository<DailySchedules, Long> {
    Optional<DailySchedules> findByDailyScheduleId(Long dailyScheduleId);
}
