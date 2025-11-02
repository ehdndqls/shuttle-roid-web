package com.ehdndqls.shuttle.organizations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    public Integer getOrganizationId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OrganizationUserDetails)) {
            throw new IllegalStateException("유효하지 않은 인증 정보입니다.");
        }

        OrganizationUserDetails userDetails = (OrganizationUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    public void setUpdateFlag(Integer organizationId) {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Optional<Organizations> organizations = organizationsRepository.findByOrganizationId(organizationId);
        String newVersion;
        String currentVersion;
        if (organizations.isPresent()) {
            Organizations organization = organizations.get();
            currentVersion = organization.getUpdateVersion();
            if(currentVersion == null || currentVersion.length() != 10) {
                organization.setUpdateVersion(todayStr + "01");
                organizationsRepository.save(organization);
                return;
            }

            String datePart = currentVersion.substring(0,8);
            String seqPartStr = currentVersion.substring(8,10);
            int seq = Integer.parseInt(seqPartStr);

            if(datePart.equals(todayStr)) {
                seq++;
                if(seq > 99) seq = 99;
            }else{
                seq = 1;
            }
            organization.setUpdateVersion(todayStr + String.format("%02d", seq));
            organizationsRepository.save(organization);
            return;
        }
    }

    // update를 해야하는 경우 true, update를 해야하는 경우가 아니거나 기관 검색에 실패할 경우 false 반환
    public boolean checkUpdateFlag(Integer organizationId, String updateVersion) {
        Optional<Organizations> organizations = organizationsRepository.findByOrganizationId(organizationId);
        if(!organizations.isPresent()) {
            return false;
        }
        Organizations organization = organizations.get();
        String currentVersion = organization.getUpdateVersion();

        return !currentVersion.equals(updateVersion);
    }
}
