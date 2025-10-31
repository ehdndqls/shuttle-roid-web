package com.ehdndqls.shuttle.drivers;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface  DriversRepository extends JpaRepository<Drivers, Integer> {
    Page<Drivers> findByOrganizationId(Integer organizationId, Pageable pageable);
    List<Drivers> findByOrganizationId(Integer organizationId);


    @Query("SELECT d FROM Drivers d WHERE " +
            "(:searchText IS NULL OR d.driverName LIKE %:searchText% OR CAST(d.employeeNumber AS string) = :searchText) AND " +
            "(:type IS NULL OR d.employmentType = :type) AND " +
            "(:joinYear IS NULL OR d.active) AND " +
            "(:organizationId IS NULL OR d.organizationId = :organizationId)")
    List<Drivers> searchDrivers(
            @Param("searchText") String searchText,
            @Param("type") Drivers.EmploymentType type,
            @Param("active") Boolean active,
            @Param("organizationId") Integer organizationId
    );

}
