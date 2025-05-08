package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.courses.Routes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
public class RouteResponseDto {
    private Long routeId;
    private String routeName;
    private Routes.RouteType routeType;
    private boolean holidayService;
    private Routes.VehicleType typeRestriction;
    private String estimatedTime;

    private List<StopDto> stops;

    public RouteResponseDto(Routes route, List<BusStops> orderedStops) {
        this.routeId = route.getRouteId();
        this.routeName = route.getRouteName();
        this.routeType = route.getRouteType();
        this.holidayService = route.getHolidayService();
        this.typeRestriction = route.getTypeRestriction();
        this.estimatedTime = "아직구현 안함요";

        // Stop -> StopDto 변환
        this.stops = orderedStops.stream()
                .map(StopDto::new)
                .collect(Collectors.toList());
    }
}
