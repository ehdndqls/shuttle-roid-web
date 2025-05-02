package com.ehdndqls.shuttle.vehicles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiclesRepository extends JpaRepository<Vehicles, Long> {

    Page<Vehicles> findByOrganizationId(Long organizationId, Pageable pageable);

    @Query("SELECT v FROM Vehicles v WHERE " +
            "(:searchText IS NULL OR v.vehicleModel LIKE %:searchText% OR v.vehicleNumber = :searchText) AND " +
            "(:vehicleType IS NULL OR v.vehicleType = :vehicleType) AND " + // 수정: v.VehicleType -> v.vehicleType
            "(:vehicleYear IS NULL OR (:vehicleYear = 2021 AND v.vehicleYear <= 2021) OR v.vehicleYear = :vehicleYear) AND " +
            "(:organizationId IS NULL OR v.organizationId = :organizationId)")
    List<Vehicles> searchVehicles(
            @Param("searchText") String searchText,
            @Param("vehicleType") Vehicles.VehicleType vehicleType,
            @Param("vehicleYear") Integer vehicleYear,
            @Param("organizationId") Long organizationId
    );
}

