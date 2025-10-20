package com.ehdndqls.shuttle.courses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class RouteDetail {
    private Integer routeId;
    private String routeName;
    private LocalTime startTime;
    private Integer estimatedTime;

    RouteDetail() {}

    RouteDetail(Integer routeId, String routeName, LocalTime startTime, Integer estimatedTime) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.startTime = startTime;
        this.estimatedTime = estimatedTime;
    }
}
