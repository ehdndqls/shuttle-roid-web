package com.ehdndqls.shuttle.courses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CourseDto {

    private Integer courseId;
    private String name;
    private Boolean holidayServiceAvailable;
    private Courses.OperationRestrictionType routeType;
    private List<RouteDetail> routeList;

    public CourseDto(){}

    public CourseDto(Integer courseId, String name, Boolean holidayServiceAvailable, Courses.OperationRestrictionType routeType, List<RouteDetail> routeList) {
        this.courseId = courseId;
        this.name = name;
        this.holidayServiceAvailable = holidayServiceAvailable;
        this.routeType = routeType;
        this.routeList = routeList;
    }
}


