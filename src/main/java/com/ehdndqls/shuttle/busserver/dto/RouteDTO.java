package com.ehdndqls.shuttle.busserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteDTO {
    private String routeID;
    private String routeName;
    private String spendTime;
    private List<Integer> stopIds;
}
