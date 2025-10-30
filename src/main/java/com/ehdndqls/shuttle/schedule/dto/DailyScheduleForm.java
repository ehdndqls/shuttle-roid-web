package com.ehdndqls.shuttle.schedule.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class DailyScheduleForm {
    private String courseNum;
    private String vehicleNum;
    private String driverName;
    private String startStop;
    private String endStop;
    private LocalTime startTime;
    private LocalTime endTime;

    public DailyScheduleForm() {}
}
