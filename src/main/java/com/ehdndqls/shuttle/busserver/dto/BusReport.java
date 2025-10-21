package com.ehdndqls.shuttle.busserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusReport {
    private String vehicleNo;
    private String route;
    private String stopLocation;
}
