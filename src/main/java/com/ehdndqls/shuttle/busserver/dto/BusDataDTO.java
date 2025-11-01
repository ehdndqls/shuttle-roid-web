package com.ehdndqls.shuttle.busserver.dto;

import lombok.Data;

import java.util.List;



@Data
public class BusDataDTO {
    private int organizationId;
    private long updateVersion;
    private List<StopDTO> stopList;
    private List<RouteDTO> routeList;
}

