package com.ehdndqls.shuttle.schedule.dto;

import com.ehdndqls.shuttle.schedule.DailySchedules;

public class RealTimeBusOperationForm {

    private String courseName;
    private String currentRoute;
    private String vehicleNum;
    private String driverName;
    private String previousStop;
    private String currentStop;
    private String nextStop;
    private DailySchedules.RouteStatus routeStatus;
}
