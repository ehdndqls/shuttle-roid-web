package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.drivers.Drivers;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BusStopForm {

    private Long stopId;
    private String stopName;
    private Double latitude;
    private Double longitude;
    private Integer sensitivity;

    // 기본 생성자
    public BusStopForm() {
    }

    // 모든 필드를 사용하는 생성자
    public BusStopForm(Long id, String stopName, Double latitude, Double longitude, Integer sensitivity) {
        this.stopId = id;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensitivity = sensitivity;
    }
}

