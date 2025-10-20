package com.ehdndqls.shuttle.drivers;


import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DriversService {

    private final DriversRepository driversRepository;

    public DriversService(DriversRepository driversRepository) {
        this.driversRepository = driversRepository;
    }

    public void modify(DriverDto driverForm, Integer organizationId) {
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
        driver.setPhoneNumber(driverForm.getPhoneNumber());
        driver.setActive(driverForm.getActive());
        driver.setEmployeeNumber(driverForm.getEmployeeNumber());
        driver.setEmploymentType(driverForm.getEmploymentType());
        driver.setSsnFront(driverForm.getSsnFront());
        
        driver.setOrganizationId(organizationId);

        // 저장
        driversRepository.save(driver);
    }

    public List<Drivers> search(String searchText, Boolean active, Drivers.EmploymentType type, Integer organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return driversRepository.searchDrivers(searchText, type, active, organizationId);
    }
}
