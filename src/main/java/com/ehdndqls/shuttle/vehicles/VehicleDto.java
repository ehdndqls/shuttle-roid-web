package com.ehdndqls.shuttle.vehicles;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VehicleDto {

    private Integer vehicleId;
    private String vehicleNumber;
    private String vehicleModel;
    private Integer vehicleYear;
    private Integer premiumCapacity;
    private Integer seatCapacity;
    private Integer standCapacity;

    // 기본 생성자
    public VehicleDto() {
    }

    // 모든 필드를 사용하는 생성자
    public VehicleDto(Integer vehicleId, String vehicleNumber, String vehicleModel, Integer vehicleYear, Integer premiumCapacity, Integer seatCapacity, Integer standCapacity) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.premiumCapacity = premiumCapacity;
        this.seatCapacity = seatCapacity;
        this.standCapacity = standCapacity;
    }

}

