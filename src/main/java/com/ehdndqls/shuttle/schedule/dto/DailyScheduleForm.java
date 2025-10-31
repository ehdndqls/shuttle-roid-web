package com.ehdndqls.shuttle.schedule.dto;

import com.ehdndqls.shuttle.schedule.DailySchedules;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class DailyScheduleForm {
    private Integer scheduleId;
    private String courseNum;
    private String vehicleNum;
    private String driverName;
    private String startRoute;
    private String endRoute;
    private LocalTime startTime;
    private LocalTime endTime;
    private DailySchedules.Status status;
    private Boolean isHoliday;

    public DailyScheduleForm() {}
}
