package com.ehdndqls.shuttle.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CurrentStop {
    private Integer currentStop;
    private String status;
    private String vehicleNum;

}
