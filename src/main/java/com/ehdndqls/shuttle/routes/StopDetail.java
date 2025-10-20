package com.ehdndqls.shuttle.routes;

import com.ehdndqls.shuttle.busstop.BusStops;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StopDetail {
    private String id;
    private String name;
    private String details;

    public StopDetail(){}

    public StopDetail(Integer organizationId, Integer stopId, String details, String name) {
        this.id = organizationId.toString() + "-" + stopId.toString();
        this.name = name;
        this.details = details;   // 시간정보
    }
}
