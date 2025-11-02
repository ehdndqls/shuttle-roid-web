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

    @GetMapping("/user/meta")
    public Map<String, Object> getMeta(Integer orgID, Integer routeID) {
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
    public List<Map<String, Object>> getStops(Integer orgID, Integer routeID) {
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
    @GetMapping("/user/vehicles")
    public List<Map<String, Object>> getVehicles(Integer orgID, Integer routeID) {

        List<DailySchedules> dailySchedules = dailyScheduleRepository
                .findByOrganizationIdAndCurrentRoute(orgID, routeID);

        if (dailySchedules == null || dailySchedules.isEmpty()) {
            throw new NoSuchElementException("Route not found");
        }

        List<Map<String, Object>> vehicles = new ArrayList<>();

        for (DailySchedules ds : dailySchedules) {
            Map<String, Object> vehicle = new HashMap<>();
            vehicle.put("stopId", orgID+'-'+ds.getCurrentStop());        // 정류장 ID
            vehicle.put("vehicleNo", vehiclesRepository.findById(ds.getVehicleId()).get().getVehicleNumber());  // 차량 번호
            vehicle.put("status", ds.getRouteStatus());
            vehicles.add(vehicle);
        }

        return vehicles;
    }

}
