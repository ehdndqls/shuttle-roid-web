package com.ehdndqls.shuttle.schedule.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DispatchScheduleDTO {
    private Integer scheduleId;
    private Integer vehicleId;
    private Integer driverId;
}
