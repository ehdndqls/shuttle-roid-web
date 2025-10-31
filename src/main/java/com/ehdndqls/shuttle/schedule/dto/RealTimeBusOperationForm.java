package com.ehdndqls.shuttle.schedule.dto;

import com.ehdndqls.shuttle.schedule.DailySchedules;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RealTimeBusOperationForm {

    private String courseName;
    private String currentRoute;
    private String vehicleNum;
    private String driverName;
    private String previousStop;
    private String currentStop;
    private String nextStop;
    private DailySchedules.RouteStatus routeStatus;

    public RealTimeBusOperationForm() {}
}
