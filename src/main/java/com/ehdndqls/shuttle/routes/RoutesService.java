package com.ehdndqls.shuttle.routes;

import com.ehdndqls.shuttle.busstop.*;
import com.ehdndqls.shuttle.busstop.RouteId;
import com.ehdndqls.shuttle.dto.RouteForm;
import com.ehdndqls.shuttle.dto.RouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutesService {

    private final BusStopsRepository busStopsRepository;
    private final RoutesRepository routesRepository;
    private final EstimatedTimeRepository estimatedTimeRepository;


    public List<RouteResponseDto> getRoutesForOrganization(Integer organizationId) {
        // organizationId가 일치하는 route들을 찾기
        List<Routes> routesList = routesRepository.findByOrganizationId(organizationId);

        // 각 Route를 DTO로 변환
        return routesList.stream()
                .map(route -> convertToRouteResponseDto(route))
                .collect(Collectors.toList());
    }

    // Todo 검색기능도 수정
    /*
    public List<RouteResponseDto> search(String searchText, Routes.RouteType routeType, String routeNum, Integer organizationId) {
        // organizationId가 1인 route들을 찾기
        List<Routes> routesList = routesRepository.searchRoutes(searchText, routeType, routeNum, organizationId);

        // 각 Route를 DTO로 변환
        return routesList.stream()
                .map(route -> convertToRouteResponseDto(route))
                .collect(Collectors.toList());
    }*/

// Todo: 여기에 organizationID를 추가로 삽입하여 검색하는 것으로 수정

    private RouteResponseDto convertToRouteResponseDto(Routes route) {
        // route의 stops를 ID로 가져오기
        List<Integer> stopIds = route.getStopList();

        // ID에 맞는 BusStops를 DB에서 조회
        List<BusStops> stopList = busStopsRepository
                .findAllByOrganizationIdAndStopIds(route.getId().getOrganizationId(),stopIds);

        // 순서대로 정렬
        List<BusStops> orderedStops = stopIds.stream()
                .map(stopId -> stopList.stream().filter(stop -> stop.getId().equals(stopId)).findFirst().orElse(null))
                .collect(Collectors.toList());

        // Route와 정렬된 stops 리스트를 DTO로 변환하여 반환
        return new RouteResponseDto(route, orderedStops);
    }

    public void modify(RouteForm routeForm, Integer organizationId) {
        Routes route;
        RouteId id;
        // 신균지 중곤지 확인
        if(routeForm.getRouteId() != null) {
            id = new RouteId(organizationId, routeForm.getRouteId());
            route = routesRepository.findById(id).orElse(null);
        }
        else{
            route = new Routes();
            id = new RouteId(organizationId, initRouteId(routeForm.getRouteNum(), routeForm.getRouteType()));
            route.setId(id);
        }

        // 값 설정
        route.setRouteNum(routeForm.getRouteNum());
        route.setRouteName(routeForm.getRouteName());
        route.setRouteType(routeForm.getRouteType());
        route.setStopList(routeForm.getStopList());
        route.setEstimatedTime(routeForm.getEstimatedTime());

        // 저장
        routesRepository.save(route);
    }

    public Integer initRouteId(String routeNum, Routes.RouteType routeType) {
        // 1. 지선 코드 정의
        Map<String, Integer> branchCodeMap = Map.of(
                "", 0,
                "A", 1, "B", 2, "C", 3,
                "-6", 4, "-5", 5, "-4", 6, "-3", 7, "-2", 8, "-1", 9
        );

        String mainRoute = routeNum;
        int branchCode = 0;

        // 2. 지선 코드 분리
        if (routeNum.matches(".*-\\d+")) { // 예: "720-1"
            mainRoute = routeNum.split("-")[0];
            branchCode = branchCodeMap.getOrDefault("-" + routeNum.split("-")[1], 0);
        } else if (routeNum.matches(".*[A-C]$")) { // 예: "110A"
            mainRoute = routeNum.substring(0, routeNum.length() - 1);
            branchCode = branchCodeMap.getOrDefault(routeNum.substring(routeNum.length() - 1), 0);
        } else {
            branchCode = 0;
        }

        // 3. 숫자 조합
        try {
            int mainNum = Integer.parseInt(mainRoute);
            return Integer.parseInt(String.format("%d%d%d", mainNum, branchCode, routeType.getCode()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid routeNum format: " + routeNum);
        }
    }

    void updateEstimatedTime(BusStopId departureStop, BusStopId arrivalStop) {
        estimatedTimeRepository
    }

}
