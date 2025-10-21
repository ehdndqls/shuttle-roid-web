package com.ehdndqls.shuttle.organizations;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationsRepository  extends JpaRepository<Organizations, Integer> {

    Optional<Organizations> findByOrganizationName(String organizationName);

    @Query("SELECT o.organizationId FROM Organizations o")
    List<Integer> findAllOrganizationIdBy();
}

