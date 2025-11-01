package com.ehdndqls.shuttle.busserver;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import com.ehdndqls.shuttle.routes.StopDetail;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BusService {
    private final BusStopsRepository busStopsRepository;
    private final RoutesRepository routesRepository;

    public Map<String, Object> getBusData(Integer organizationId) {

        List<BusStops> stops = busStopsRepository.findById_OrganizationId(organizationId);
        List<Routes> routes = routesRepository.findById_OrganizationId(organizationId);

        List<Map<String, Object>> stopList = new ArrayList<>();
        List<Map<String, Object>> routeList = new ArrayList<>();

        for (BusStops s : stops) {
            Map<String, Object> stopJson = new LinkedHashMap<>();
            stopJson.put("stopID", String.format("%03d", s.getId().getOrganizationId())
                    + String.format("%04d", s.getId().getStopId()));
            stopJson.put("stopName", s.getStopName());
            stopJson.put("latitude", s.getLatitude());
            stopJson.put("longitude", s.getLongitude());
            stopJson.put("approach", s.getApproach());
            stopJson.put("arrival", s.getArrival());
            stopJson.put("leave", s.getDeparture());
            stopList.add(stopJson);
        }

        for (Routes r : routes) {
            Map<String, Object> routeJson = new LinkedHashMap<>();
            routeJson.put("routeID", r.getId().getRouteId());
            routeJson.put("routeName", r.getRouteName());
            routeJson.put("spendTime", r.getEstimatedTime());

            List<String> stopIds = r.getStopList().stream()
                    .map(stop -> {
                        String[] parts = stop.getId().split("-"); // "1-9001"
                        int orgId = Integer.parseInt(parts[0]);
                        int stopId = Integer.parseInt(parts[1]);
                        return String.format("%03d%04d", orgId, stopId); // 0019001
                    })
                    .collect(Collectors.toList());

            System.out.println(stopIds); // ["1010001", "1019001", ...]
            routeJson.put("stopIds", stopIds); // 여기서 JSON 배열 형태로 넣기
            routeList.add(routeJson);          // routeList에 추가
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("organizationId", organizationId);
        // Todo: updateVersion
        result.put("updateVersion", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd01")));
        result.put("stopList", stopList);
        result.put("routeList", routeList);

        return result;
    }

//    public ObjectNode convertToStopIdJson(List<StopDetail> stopList) {
//        ObjectNode routeJson = objectMapper.createObjectNode();
//        ArrayNode stopIds = objectMapper.createArrayNode();
//
//
//        for (StopDetail s : stopList) {
//            // "1-9001" → [orgId=1, stopId=9001]
//            String[] parts = s.getId().split("-");
//            if (parts.length == 2) {
//                try {
//                    int orgId = Integer.parseInt(parts[0]);
//                    int stopId = Integer.parseInt(parts[1]);
//
//                    // ID 조합: orgId(3자리) + stopId(4자리)
//                    long combinedId = Long.parseLong(String.format("%03d%04d", orgId, stopId));
//                    stopIds.add(combinedId);
//                } catch (NumberFormatException e) {
//                    // 파싱 실패 시 무시 (로그만 남기기)
//                    System.err.println("Invalid stop id format: " + s.getId());
//                }
//            }
//        }
//
//        routeJson.set("stopIds", stopIds);
//        return routeJson;
//    }
}

