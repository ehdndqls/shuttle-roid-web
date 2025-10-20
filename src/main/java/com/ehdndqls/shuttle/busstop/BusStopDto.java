package com.ehdndqls.shuttle.busstop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BusStopDto {

    private Integer stopId;
    private String stopName;
    private Double latitude;
    private Double longitude;
    private Integer approach;
    private Integer arrival;
    private Integer departure;
    private Boolean via;

    // 기본 생성자
    public BusStopDto() {
    }

    // 모든 필드를 사용하는 생성자
    public BusStopDto(Integer id,
                      String stopName,
                      Double latitude,
                      Double longitude,
                      Boolean via,
                      Integer approach, Integer arrival, Integer departure) {
        this.stopId = id;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.approach = approach;
        this.arrival = arrival;
        this.departure = departure;
        if(via != null)
            this.via = true;
        else
            this.via = false;
    }
}

