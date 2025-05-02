package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.vehicles.Vehicles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VehicleForm {

    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleModel;
    private Integer vehicleYear;
    private Vehicles.VehicleType vehicleType;
    private Integer seatCapacity;
    private Integer standCapacity;

    // 기본 생성자
    public VehicleForm() {
    }

    // 모든 필드를 사용하는 생성자
    public VehicleForm(Long vehicleId, String vehicleNumber, String vehicleModel, Integer vehicleYear, Vehicles.VehicleType vehicleType, Integer seatCapacity, Integer standCapacity) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.vehicleType = vehicleType;
        this.seatCapacity = seatCapacity;
        this.standCapacity = standCapacity;
    }

}

