package com.ehdndqls.shuttle.courses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Courses, CourseId> {
    Page<Courses> findById_OrganizationId(Integer organizationId, Pageable pageable);

    @Query("SELECT MAX(c.id.courseId) FROM Courses c WHERE c.id.organizationId = :organizationId")
    Integer findMaxCourseIdByOrganizationId(@Param("organizationId") Integer organizationId);

    @Query("SELECT c FROM Courses c WHERE c.id.organizationId = :organizationId AND c.holidayServiceAvailable = :holidayServiceAvailable")
    List<Courses> findByOrganizationIdAndIsHoliday(
            @Param("organizationId") Integer organizationId,
            @Param("holidayServiceAvailable") Boolean holidayServiceAvailable
    );
}
