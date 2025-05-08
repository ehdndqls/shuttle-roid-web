package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import com.ehdndqls.shuttle.dto.RouteResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoutesService {

    private final BusStopsRepository busStopsRepository;
    private final RoutesRepository routesRepository;

    public RoutesService(RoutesRepository routesRepository, BusStopsRepository busStopsRepository) {
        this.routesRepository = routesRepository;
        this.busStopsRepository = busStopsRepository;
    }


    public List<RouteResponseDto> getRoutesForOrganization(Long organizationId) {
        // organizationId가 1인 route들을 찾기
        List<Routes> routesList = routesRepository.findByOrganizationId(organizationId);

        // 각 Route를 DTO로 변환
        return routesList.stream()
                .map(route -> convertToRouteResponseDto(route))
                .collect(Collectors.toList());
    }


    private RouteResponseDto convertToRouteResponseDto(Routes route) {
        // route의 stops를 ID로 가져오기
        List<Long> stopIds = route.getStops();

        // ID에 맞는 BusStops를 DB에서 조회
        List<BusStops> stopList = busStopsRepository.findAllById(stopIds);

        // 순서대로 정렬
        List<BusStops> orderedStops = stopIds.stream()
                .map(stopId -> stopList.stream().filter(stop -> stop.getStopId().equals(stopId)).findFirst().orElse(null))
                .collect(Collectors.toList());

        // Route와 정렬된 stops 리스트를 DTO로 변환하여 반환
        return new RouteResponseDto(route, orderedStops);
    }


}
