package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.dto.DriverForm;
import com.ehdndqls.shuttle.dto.RouteForm;
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

    public List<RouteResponseDto> search(String searchText, Boolean holiday, Routes.RouteType routeType, Long organizationId) {
        // organizationId가 1인 route들을 찾기
        List<Routes> routesList = routesRepository.searchRoutes(searchText, routeType, holiday, organizationId);

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

    public void modify(RouteForm routeForm, Long organizationId) {
        Routes route;
        // 신균지 중곤지 확인
        if(routeForm.getRouteId() != null) {
            route = routesRepository.findById(routeForm.getRouteId()).orElse(null);
        }
        else{
            route = new Routes();
        }

        // 값 설정
        route.setRouteName(routeForm.getRouteName());
        route.setRouteType(routeForm.getRouteType());
        route.setHolidayService(routeForm.getHolidayService());
        route.setTypeRestriction(routeForm.getTypeRestriction());
        route.setStops(routeForm.getStops());

        route.setOrganizationId(organizationId);

        // 저장
        routesRepository.save(route);
    }

}
