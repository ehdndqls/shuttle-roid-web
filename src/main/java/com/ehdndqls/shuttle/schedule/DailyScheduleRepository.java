package com.ehdndqls.shuttle.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailySchedules, Integer> {
    List<DailySchedules> findByDate(LocalDate idDate);
    DailySchedules findByDateAndOrganizationIdAndCourseId(LocalDate idDate, Integer organizationId, Integer courseId);
}
