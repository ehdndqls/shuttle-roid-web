package com.ehdndqls.shuttle.organizations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    public Long getOrganizationId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OrganizationUserDetails)) {
            throw new IllegalStateException("유효하지 않은 인증 정보입니다.");
        }

        OrganizationUserDetails userDetails = (OrganizationUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }
}
