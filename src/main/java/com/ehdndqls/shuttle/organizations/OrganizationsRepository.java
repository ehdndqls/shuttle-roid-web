package com.ehdndqls.shuttle.organizations;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationsRepository  extends JpaRepository<Organizations, Long> {

    Optional<Organizations> findByOrganizationName(String organizationName);
}

