package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.RouteId;
import com.ehdndqls.shuttle.routes.Routes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
public class RouteResponseDto {
    private RouteId routeId;
    private String routeNum;
    private String routeName;
    private Routes.RouteType routeType;
    private String estimatedTime;

    private List<StopDto> stops;

    public RouteResponseDto(Routes route, List<BusStops> orderedStops) {
        this.routeId = route.getId();
        this.routeName = route.getRouteName();
        this.routeType = route.getRouteType();
        this.estimatedTime = route.getEstimatedTime();

        // Stop -> StopDto 변환
        this.stops = orderedStops.stream()
                .map(StopDto::new)
                .collect(Collectors.toList());
    }
}
