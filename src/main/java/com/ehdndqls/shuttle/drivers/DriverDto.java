package com.ehdndqls.shuttle.drivers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DriverDto {

    private Integer id;
    private String driverName;
    private String phoneNumber;
    private String employeeNumber;
    private Drivers.EmploymentType employmentType;
    private String ssnFront;
    private Boolean active;

    // 기본 생성자
    public DriverDto() {
    }

    // 모든 필드를 사용하는 생성자
    public DriverDto(Integer id, String driverName, String phoneNumber, String employeeNumber, String ssnFront, Drivers.EmploymentType employmentType, Boolean active) {
        this.id = id;
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.employeeNumber = employeeNumber;
        this.employmentType = employmentType;
        this.ssnFront = ssnFront;
        this.active = active;
    }
}