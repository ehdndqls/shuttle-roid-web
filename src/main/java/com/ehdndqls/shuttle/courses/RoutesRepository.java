package com.ehdndqls.shuttle.courses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutesRepository extends JpaRepository<Routes, Long> {
    List<Routes> findByOrganizationId(Long organizationId);
}

