package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.drivers.Drivers;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BusStopForm {

    private Integer stopId;
    private String stopName;
    private Double latitude;
    private Double longitude;
    private Integer approach;
    private Integer arrival;
    private Integer leave;
    private Boolean via;

    // 기본 생성자
    public BusStopForm() {
    }

    // 모든 필드를 사용하는 생성자
    public BusStopForm(Integer id,
                       String stopName,
                       Double latitude,
                       Double longitude,
                       Boolean via,
                       Integer approach, Integer arrival, Integer leave) {
        this.stopId = id;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.approach = approach;
        this.arrival = arrival;
        this.leave = leave;
        this.via = via;
    }
}

