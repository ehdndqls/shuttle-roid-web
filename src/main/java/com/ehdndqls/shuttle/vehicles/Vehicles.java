package com.ehdndqls.shuttle.vehicles;

import com.ehdndqls.shuttle.organizations.Organizations;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class Vehicles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;

    @Column(nullable = false)
    private Integer organizationId;

    private String vehicleNumber;
    private Integer seatCapacity;
    private Integer standCapacity;
    private Integer premiumCapacity;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private VehicleType vehicleType;
    private String vehicleModel;
    private Integer vehicleYear;


    public enum VehicleType {
        SMALL, // 소형 15인승 이하 1종 보통
        LARGE   // 대형 16인승 이상 1종 대형
    }

}
