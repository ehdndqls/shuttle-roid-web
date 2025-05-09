package com.ehdndqls.shuttle.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoutesRepository extends JpaRepository<Routes, Long> {
    List<Routes> findByOrganizationId(Long organizationId);

    @Query("SELECT r FROM Routes r WHERE " +
            "(:searchText IS NULL OR r.routeName LIKE %:searchText% OR str(r.routeId) = :searchText) AND " +
            "(:routeType IS NULL OR r.routeType = :routeType) AND " +
            "(:holiday IS NULL OR r.holidayService = :holiday) AND " +
            "(:organizationId IS NULL OR r.organizationId = :organizationId)")
    List<Routes> searchRoutes(
            @Param("searchText") String searchText,
            @Param("routeType") Routes.RouteType type,
            @Param("holiday") Boolean holiday,
            @Param("organizationId") Long organizationId
    );
}

