package com.ehdndqls.shuttle.vehicles;

import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class VehiclesService {

    private final VehiclesRepository vehiclesRepository;

    public VehiclesService(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    public void modify(VehicleDto vehicleForm, Integer organizationId) {
        Vehicles vehicle;
        // 신균지 중곤지 확인
        if(vehicleForm.getVehicleId() != null) {
            vehicle = vehiclesRepository.findById(vehicleForm.getVehicleId()).orElse(null);
        }
        else{
            vehicle = new Vehicles();
        }

        // 차량 타입 설정 ( 대형 | 소형 )
        if(vehicleForm.getSeatCapacity() > 15)
            vehicle.setVehicleType(Vehicles.VehicleType.LARGE);
        else
            vehicle.setVehicleType(Vehicles.VehicleType.SMALL);

        // 값 설정
        vehicle.setVehicleNumber(vehicleForm.getVehicleNumber());
        vehicle.setVehicleModel(vehicleForm.getVehicleModel());
        vehicle.setVehicleYear(vehicleForm.getVehicleYear());
        vehicle.setSeatCapacity(vehicleForm.getSeatCapacity());
        vehicle.setStandCapacity(vehicleForm.getStandCapacity());
        vehicle.setPremiumCapacity(vehicleForm.getPremiumCapacity());

        vehicle.setOrganizationId(organizationId);

        // 저장
        vehiclesRepository.save(vehicle);
    }

    public List<Vehicles> search(String searchText, Vehicles.VehicleType vehicleType, Integer vehicleYear, Integer organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return vehiclesRepository.searchVehicles(searchText, vehicleType, vehicleYear, organizationId);
    }

}
