package com.ehdndqls.shuttle;

import com.ehdndqls.shuttle.organizations.OrganizationUserDetails;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final OrganizationsRepository organizationsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Organizations organization = organizationsRepository.findByOrganizationName(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 기관명을 찾을 수 없습니다: " + username));
        return new OrganizationUserDetails(organization);
    }
}
