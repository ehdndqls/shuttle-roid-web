package com.ehdndqls.shuttle.vehicles;

import com.ehdndqls.shuttle.dto.VehicleForm;
import com.ehdndqls.shuttle.organizations.OrganizationUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class VehiclesService {

    private final VehiclesRepository vehiclesRepository;

    public VehiclesService(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    public void modify(VehicleForm vehicleForm, Long organizationId) {
        Vehicles vehicle;
        // 신균지 중곤지 확인
        if(vehicleForm.getVehicleId() != null) {
            vehicle = vehiclesRepository.findById(vehicleForm.getVehicleId()).orElse(null);
        }
        else{
            vehicle = new Vehicles();
        }

        // 값 설정
        vehicle.setVehicleNumber(vehicleForm.getVehicleNumber());
        vehicle.setVehicleType(vehicleForm.getVehicleType());
        vehicle.setVehicleModel(vehicleForm.getVehicleModel());
        vehicle.setVehicleYear(vehicleForm.getVehicleYear());
        vehicle.setSeatCapacity(vehicleForm.getSeatCapacity());
        vehicle.setStandCapacity(vehicleForm.getStandCapacity());

        vehicle.setOrganizationId(organizationId);

        // 저장
        vehiclesRepository.save(vehicle);
    }

    public List<Vehicles> search(String searchText, Vehicles.VehicleType vehicleType, Integer vehicleYear, Long organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return vehiclesRepository.searchVehicles(searchText, vehicleType, vehicleYear, organizationId);
    }

}
