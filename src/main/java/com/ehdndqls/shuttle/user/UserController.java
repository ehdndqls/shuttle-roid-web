package com.ehdndqls.shuttle.user;

import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.routes.RouteId;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import com.ehdndqls.shuttle.routes.StopDetail;
import com.ehdndqls.shuttle.schedule.DailyScheduleRepository;
import com.ehdndqls.shuttle.schedule.DailySchedules;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserController {

   // private final OrganizationsRepository organizationsRepository;
    private final UserService userService;
    private final RoutesRepository routesRepository;
    private final DailyScheduleRepository dailyScheduleRepository;
    private final VehiclesRepository vehiclesRepository;
    private final OrganizationsRepository organizationsRepository;

    @GetMapping("/user/init")
    public String init(Model model) {
        model.addAttribute("orgs", userService.getOrganizations());
        return "user-init.html";
    }

    @GetMapping("/user")
    public String user(Model model, @RequestParam int orgID) {
        model.addAttribute("routeList", routesRepository.findById_OrganizationId(orgID));
        return "user-main.html";
    }

    @GetMapping("/user/route-detail")
    public String routeDetail(Model model, @RequestParam int orgId, @RequestParam int routeId) {
        Optional<Routes> optionalRoute = routesRepository.findById(new RouteId(orgId, routeId));
        model.addAttribute("route", optionalRoute.orElse(null));
        model.addAttribute("status", userService.getCurrentStops(orgId, routeId));

        return "user-route-detail.html";
    }

    @GetMapping("/user/route-list")
    @ResponseBody
    public List<Map<String, Object>> routeList(@RequestParam int orgID) {
        List<Routes> routes = routesRepository.findById_OrganizationId(orgID);

        List<Map<String, Object>> routeData = new ArrayList<>();

        for (Routes r : routes) {
            Map<String, Object> routeMap = new HashMap<>();
            routeMap.put("routeId", r.getId().getRouteId());          // 복합키에서 routeId
            routeMap.put("routeNumber", r.getRouteNum());            // 노선 번호
            routeMap.put("routeTitle", r.getRouteName());            // 노선 이름
            routeMap.put("routeType", r.getRouteType().toString());  // RouteType 문자열
            routeMap.put("estimatedTime", r.getEstimatedTime());    // 예상 소요 시간
            routeData.add(routeMap);
        }

        return routeData;
    }

    @GetMapping("/user/meta")
    @ResponseBody
    public Map<String, Object> getMeta(@RequestParam Integer orgID, @RequestParam Integer routeID) {
        Routes route = routesRepository.findById(new RouteId(orgID, routeID))
                .orElseThrow(() -> new NoSuchElementException("Route not found"));

        Map<String, Object> meta = new HashMap<>();
        meta.put("routeNumber", route.getRouteNum());
        meta.put("directionLabel", route.getRouteType());
        meta.put("routeTitle", route.getRouteName());
        meta.put("serviceArea", organizationsRepository.findByOrganizationId(orgID).get().getOrganizationName());
        meta.put("firstTime", "06:20");
        meta.put("lastTime", "22:30");
        meta.put("intervalText", "매일 10–30분");
        return meta;
    }

    // 2. /user/stops
    @GetMapping("/user/stops")
    @ResponseBody
    public List<Map<String, Object>> getStops(@RequestParam Integer orgID, @RequestParam Integer routeID) {
        // DB에서 route 조회
        Routes route = routesRepository.findById(new RouteId(orgID, routeID))
                .orElseThrow(() -> new NoSuchElementException("Route not found"));

        List<StopDetail> stopList = route.getStopList();
        List<Map<String, Object>> result = new ArrayList<>();

        for (StopDetail s : stopList) {
            Map<String, Object> stop = new HashMap<>();
            stop.put("id", s.getId());
            stop.put("name", s.getName());
            result.add(stop);
        }

        return result;
    }

    // 3. /user/vehicles
    @ResponseBody
    @GetMapping("/user/vehicles")
    public List<Map<String, Object>> getVehicles(@RequestParam Integer orgID, @RequestParam Integer routeID) {

        List<DailySchedules> dailySchedules = dailyScheduleRepository
                .findByOrganizationIdAndCurrentRoute(orgID, routeID);

        if (dailySchedules == null || dailySchedules.isEmpty()) {
            throw new NoSuchElementException("Route not found");
        }

        List<Map<String, Object>> vehicles = new ArrayList<>();

        for (DailySchedules ds : dailySchedules) {
            Map<String, Object> vehicle = new HashMap<>();
            vehicle.put("stopId", String.valueOf(orgID)+'-'+ds.getCurrentStop());        // 정류장 ID
            vehicle.put("vehicleNo", vehiclesRepository.findById(ds.getVehicleId()).get().getVehicleNumber());  // 차량 번호
            vehicle.put("status", ds.getRouteStatus());
            vehicles.add(vehicle);
        }

        return vehicles;
    }

}
