package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.drivers.Drivers;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DriverForm {

    private Long id;
    private String driverName;
    private Integer joinYear;
    private Drivers.DriverType type;
    private Boolean active;

    // 기본 생성자
    public DriverForm() {
    }

    // 모든 필드를 사용하는 생성자
    public DriverForm(Long id, String driverName, Integer joinYear, Drivers.DriverType type, Boolean active) {
        this.id = id;
        this.driverName = driverName;
        this.joinYear = joinYear;
        this.type = type;
        this.active = active;
    }
}