package com.ehdndqls.shuttle.busserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteReport {
    private Integer orgID;
    private Integer courseID;
    private Integer routeID;
    private String departTime;
    private Integer vehicleID;
    private boolean flag;
}
