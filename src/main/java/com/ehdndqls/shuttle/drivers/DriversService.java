package com.ehdndqls.shuttle.drivers;


import com.ehdndqls.shuttle.dto.DriverForm;
import com.ehdndqls.shuttle.organizations.OrganizationUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DriversService {

    private final DriversRepository driversRepository;

    public DriversService(DriversRepository driversRepository) {
        this.driversRepository = driversRepository;
    }

    public void modify(DriverForm driverForm, Long organizationId) {
        Drivers driver;
        // 신균지 중곤지 확인
        if(driverForm.getId() != null) {
            driver = driversRepository.findById(driverForm.getId()).orElse(null);
        }
        else{
            driver = new Drivers();
        }

        // 값 설정
        driver.setDriverName(driverForm.getDriverName());
        driver.setActive(driverForm.getActive());
        driver.setType(driverForm.getType());
        driver.setJoinYear(driverForm.getJoinYear());
        
        driver.setOrganizationId(organizationId);

        // 저장
        driversRepository.save(driver);
    }

    public List<Drivers> search(String searchText, Drivers.DriverType type, Integer joinYear, Long organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return driversRepository.searchDrivers(searchText, type, joinYear, organizationId);
    }

    public Long getOrganizationId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OrganizationUserDetails)) {
            throw new IllegalStateException("유효하지 않은 인증 정보입니다.");
        }

        OrganizationUserDetails userDetails = (OrganizationUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }
}
