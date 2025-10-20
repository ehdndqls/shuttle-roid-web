package com.ehdndqls.shuttle.routes;

import com.ehdndqls.shuttle.busstop.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutesService {

    private final BusStopsRepository busStopsRepository;
    private final RoutesRepository routesRepository;
    private final TimeDetailRepository timeDetailRepository;



    public List<Routes> search(String searchText, Routes.RouteType routeType, Integer organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return routesRepository.searchRoutes(searchText, routeType, organizationId);
    }

    public void modify(RouteDto routeForm, Integer organizationId) {
        Routes route;
        RouteId id;
        // 신균지 중곤지 확인
        // 수정 요청일 경우 아이디를 검색해서 기존 route를 불러옴
        if(routeForm.getRouteId() != null) {
            id = new RouteId(organizationId, routeForm.getRouteId());
            route = routesRepository.findById(id).orElse(null);
        }
        // 신규 생성일 경우 새로운 루트와 ID를 생성함
        else{
            route = new Routes();
            id = new RouteId(organizationId, initRouteId(routeForm.getRouteNum(), routeForm.getRouteType()));
            route.setId(id);
        }

        // 값 설정
        route.setRouteNum(routeForm.getRouteNum());
        route.setRouteName(routeForm.getRouteName());
        route.setRouteType(routeForm.getRouteType());
        route.setStopList(modifyStopList(routeForm.getStopList(), organizationId, routeForm.getEstimatedTime()));
        route.setEstimatedTime(routeForm.getEstimatedTime());

        // 저장
        routesRepository.save(route);
    }


    // 각 구간 별 소요시간 초기화 하는 함수
    public List<Integer> initStopDetails(List<Integer> stopIds, Integer organizationId, Integer estimatedTime) {
        //Todo:
        // 1. 구간 개수(routeIds.size -1) 만큼의 정수 배열 2개 생성 (하나는 직선거리, 다른 하나는 구간 별 소요시간)
        // 2. 총 소요시간에서 구간 개수 * 정차시간을 제외한 시간 저장
        // 3. 남은 시간을 각 직선거리의 비율만큼 나눠가짐

        // 구간 별 소요 시간
        List<Integer> travelTimes = new ArrayList<>();

        // 1. 각 정류소의 위도/경도 불러오기
        List<double[]> coordinates = stopIds.stream()
                .map(stopId ->{
                    BusStops stop = busStopsRepository.findById(new BusStopId(stopId, organizationId))
                            .orElse(null);
                    if (stop == null) return new double[]{0, 0};
                    return new double[]{stop.getLatitude(), stop.getLongitude()};
                })
                .toList();

        // 2. 각 구간 직선 거리 계산 (Haversine formula)
        List<Double> distances = new ArrayList<>();
        for (int i = 0; i < coordinates.size() - 1; i++) {
            double d = calcDistance(
                    coordinates.get(i)[0], coordinates.get(i)[1],
                    coordinates.get(i + 1)[0], coordinates.get(i + 1)[1]
            );
            distances.add(d);
        }

        // 3. 정차 시간 설정 (단위: 분)
        double stopDuration = 0.25; // 15초

        // 4. 총 거리합 계산
        double totalDistance = distances.stream().mapToDouble(Double::doubleValue).sum();

        // 5. 실제 주행시간 (정차 제외)
        double effectiveTime = estimatedTime - (stopIds.size() * stopDuration);

        // 6. 구간별 비율에 따른 시간 분배
        for (double d : distances) {
            double ratio = d / totalDistance;
            double timeForSection = effectiveTime * ratio + stopDuration;
            travelTimes.add((int) Math.round(timeForSection)); // 정수화
        }

        return travelTimes;
    }

    // Haversine formula
    public static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 거리 (km)
    }

    // StopList 갱신하는 함수
    public List<StopDetail> modifyStopList(List<Integer> stopIds, Integer organizationId, Integer estimatedTime) {
        List<StopDetail> stopList = new ArrayList<>();
        String timeDetail;
        // 구간 별 소요시간 초기화
        List<Integer> stopDetails = initStopDetails(stopIds, organizationId, estimatedTime);

        Integer stopId, nextStopId;

        for (int i = 0; i < stopIds.size(); i++) {
            stopId = stopIds.get(i);
            nextStopId = (i < stopIds.size() - 1) ? stopIds.get(i + 1) : null;

            timeDetail = initArrivalTime(organizationId, stopId, nextStopId);
            if (timeDetail == null){
                timeDetail = "소요시간 " + stopDetails.get(i) + "분";
            }

            BusStops busStop = busStopsRepository.findById(new BusStopId(organizationId, stopId))
                    .orElse(null);

            stopList.add(new StopDetail(
                    organizationId,
                    stopId,
                    timeDetail,
                    (busStop != null) ? busStop.getStopName() : "정류소 로딩 실패"
            ));
        }

        return stopList;
    }

    public String initArrivalTime(Integer organizationId, Integer stopId, Integer nextStopId) {
        if(nextStopId != null){
            Optional<Integer> optDetail =
                    timeDetailRepository.findTravelTime(organizationId, stopId, nextStopId);

            return optDetail
                    .map(td-> "소요시간:" + td +"분")
                            .orElse(null);
        }
        else return "종착점";
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


    //Todo:: 추후 로그에서 시간 디테일 업데이트
    void updateEstimatedTime(BusStopId departureStop, BusStopId arrivalStop) {
        //estimatedTimeRepository
    }

    void initArriverTime(){

    }

}
