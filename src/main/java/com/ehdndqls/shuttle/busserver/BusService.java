package com.ehdndqls.shuttle.busserver;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import com.ehdndqls.shuttle.courses.CourseId;
import com.ehdndqls.shuttle.courses.CourseRepository;
import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.courses.RouteDetail;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.organizations.OrganizationsService;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import com.ehdndqls.shuttle.routes.StopDetail;
import com.ehdndqls.shuttle.schedule.DailyScheduleRepository;
import com.ehdndqls.shuttle.schedule.DailySchedules;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BusService {
    private final BusStopsRepository busStopsRepository;
    private final RoutesRepository routesRepository;
    private final CourseRepository courseRepository;
    private final DailyScheduleRepository dailyScheduleRepository;
    private final OrganizationsService organizationsService;
    private final OrganizationsRepository organizationsRepository;

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
        result.put("updateVersion", organizationsRepository.findByOrganizationId(organizationId).get().getUpdateVersion());
        result.put("stopList", stopList);
        result.put("routeList", routeList);

        return result;
    }

    public List<Map<String, Object>> getCourseData(CourseId id) {

        Optional<Courses> optCourse = courseRepository.findById(id);

        if (optCourse.isEmpty()) {
            return Collections.emptyList();
        }

        Courses course = optCourse.get();

        List<Map<String, Object>> departureList = new ArrayList<>();


        for(RouteDetail r : course.getRouteList()) {
            // routeId가 0이거나 startTime이 없는 경우 스킵 -- 휴식이 routeId가 0으로 지정되어있음
            if(r.getRouteId() == null || r.getRouteId() == 0) continue;
            if(r.getStartTime() == null) continue;

            Map<String, Object> routeJson = new LinkedHashMap<>();
            routeJson.put("routeID", r.getRouteId().toString());
            routeJson.put("departureTime", r.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));

            departureList.add(routeJson);

        }
        return departureList;
    }

    public CourseId findCourseId(Integer organizationId, Integer driverId) {
        DailySchedules schedules = dailyScheduleRepository.findByOrganizationIdAndDriverId(organizationId, driverId);
        return new CourseId(organizationId, schedules.getCourseId());
    }
}

