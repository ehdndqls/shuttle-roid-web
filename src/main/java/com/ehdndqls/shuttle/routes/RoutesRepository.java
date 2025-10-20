package com.ehdndqls.shuttle.routes;

import com.ehdndqls.shuttle.routes.RouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoutesRepository extends JpaRepository<Routes, RouteId> {
    // 수정된 메서드
    List<Routes> findById_OrganizationId(Integer organizationId);

    @Query("SELECT r FROM Routes r WHERE " +
            "(:searchText IS NULL OR r.routeName LIKE %:searchText% OR r.routeNum LIKE %:searchText% OR str(r.id.routeId) = :searchText) AND " +
            "(:routeType IS NULL OR r.routeType = :routeType) AND " +
            "(:organizationId IS NULL OR r.id.organizationId = :organizationId)")
    List<Routes> searchRoutes(
            @Param("searchText") String searchText,
            @Param("routeType") Routes.RouteType type,
            @Param("organizationId") Integer organizationId
    );



}


