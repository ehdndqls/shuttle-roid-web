package com.ehdndqls.shuttle.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailySchedules, Integer> {


    Optional<DailySchedules> findByScheduleId(Integer scheduleId);
    List<DailySchedules> findByIsHolidayAndOrganizationId(Boolean isHoliday, Integer organizationId);
    List<DailySchedules> findByOrganizationId(Integer organizationId);
    DailySchedules findByIsHolidayAndOrganizationIdAndCourseId(Boolean isHoliday, Integer organizationId, Integer courseId);
    DailySchedules findByIsHolidayAndOrganizationIdAndVehicleId(Boolean isHoliday, Integer organizationId, Integer vehicleId);
    Optional<DailySchedules> findByCourseIdAndOrganizationId(Integer courseId, Integer organizationId);

    List<DailySchedules> findByOrganizationIdAndCurrentRoute(Integer organizationId, Integer routeId);
}
