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
    private Long vehicleId;

    @Column(nullable = false)
    private Long organizationId;

    private String vehicleNumber;
    private Integer seatCapacity;
    private Integer standCapacity;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private VehicleType vehicleType;
    private String vehicleModel;
    private Integer vehicleYear;


    public enum VehicleType {
        INNER_CITY, // 시내
        OUTER_CITY //시외
    }

}
