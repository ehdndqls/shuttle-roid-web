package com.ehdndqls.shuttle.drivers;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface  DriversRepository extends JpaRepository<Drivers, Long> {
    Page<Drivers> findByOrganizationId(Long organizationId, Pageable pageable);


    @Query("SELECT d FROM Drivers d WHERE " +
            "(:searchText IS NULL OR d.driverName LIKE %:searchText% OR CAST(d.driverId AS string) = :searchText) AND " +
            "(:type IS NULL OR d.type = :type) AND " +
            "(:joinYear IS NULL OR (:joinYear = 2021 AND d.joinYear <= 2021) OR d.joinYear = :joinYear) AND " +
            "(:organizationId IS NULL OR d.organizationId = :organizationId)")
    List<Drivers> searchDrivers(
            @Param("searchText") String searchText,
            @Param("type") Drivers.DriverType type,
            @Param("joinYear") Integer joinYear,
            @Param("organizationId") Long organizationId
    );

}
