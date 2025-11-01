package com.ehdndqls.shuttle.busserver.dto;

import lombok.Data;

@Data
public class StopDTO {
    private String stopID;
    private String stopName;
    private String latitude;
    private String longitude;
    private String approach;
    private String arrival;
    private String leave;
}
