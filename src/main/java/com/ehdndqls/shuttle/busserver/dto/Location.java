package com.ehdndqls.shuttle.busserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Location {
    private Integer orgID;
    private Integer vehicleID;
    private int stopID;
    private String status;
}
