package com.ehdndqls.shuttle.organizations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationsService {
    private final OrganizationsRepository organizationsRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveOrganization(String organizationName,
                                 String organizationPassword) throws Exception{
        var result = organizationsRepository.findByOrganizationName(organizationName);
        if(result.isPresent()){
            throw new IllegalArgumentException("Organization already exists");
        }

        Organizations organization = new Organizations();
        organization.setOrganizationName(organizationName);
        var hash = passwordEncoder.encode(organizationPassword);
        organization.setOrganizationPassword(hash);
        organizationsRepository.save(organization);
    }
}
