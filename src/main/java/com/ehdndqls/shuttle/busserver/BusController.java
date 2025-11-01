package com.ehdndqls.shuttle.busserver;

import com.ehdndqls.shuttle.busserver.dto.Location;
import com.ehdndqls.shuttle.busserver.dto.LoginReq;
import com.ehdndqls.shuttle.busserver.dto.OrgCheckReq;
import com.ehdndqls.shuttle.busserver.dto.RouteReport;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.schedule.DailyScheduleRepository;
import com.ehdndqls.shuttle.schedule.DailyScheduleService;
import com.ehdndqls.shuttle.schedule.DailySchedules;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BusController {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final DailyScheduleRepository dailyScheduleRepository;
    private final DailyScheduleService dailyScheduleService;
    private final OrganizationsRepository organizationsRepository;
    private final VehiclesRepository vehiclesRepository;

    //임시
    @PostMapping("/bus/org/check")
    public ResponseEntity<Map<String, String>> check(@RequestBody OrgCheckReq orgCheckReqReq) {
        Optional<Organizations> OptOrg;
        OptOrg = organizationsRepository.findByOrganizationId(orgCheckReqReq.getOrgID());
        if (OptOrg.isPresent()) {
            Organizations Org = OptOrg.get();
            return ResponseEntity.ok(Map.of("orgName", Org.getOrganizationName()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Organization not found"));
    }

    // 로그인
    @PostMapping("/bus/auth/login")
    public  ResponseEntity<Map<String, String>> login(@RequestBody LoginReq req) {
        System.out.println("[Login Request] OrgID: " + req.getOrgID() + ", DriverID: " + req.getDriverID());
        // Todo: 이거 데이터 뽑아서 차량번호 보내기
        Optional<DailySchedules> OptSchedule;
        OptSchedule = dailyScheduleRepository.findByDriverIdAndOrganizationId(req.getDriverID(), req.getOrgID());
        if (OptSchedule.isPresent()) {
            DailySchedules ds = OptSchedule.get();
            Optional<Vehicles> OptVehicle = vehiclesRepository.findById(ds.getVehicleId());
            if (OptVehicle.isPresent()) {
                return ResponseEntity.ok(Map.of("vehicleNum", OptVehicle.get().getVehicleNumber()));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Vehicle not found"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Schedule not found"));
    }

    // Todo: 여기부터 하시면 됩니당
    // 데이터 업데이트
    @GetMapping("/bus/update")
    @ResponseBody
    public Object update(@RequestParam int orgID, @RequestParam int dataVer) throws IOException {
        System.out.println("[Update Request] OrgID: " + orgID + ", DataVer: " + dataVer);
        return readJson("src/main/resources/data.json");
    }

    // 스케줄 요청
    @GetMapping("/bus/schedule")
    @ResponseBody
    public Object schedule(@RequestParam int orgID, @RequestParam int driverID) throws IOException {
        System.out.println("[Scheduling Request] OrgID: " + orgID + ", DriverID: " + driverID);
        return readJson("src/main/resources/101001.json");
    }

    // 위치 이벤트
    @PostMapping("/bus/location")
    public ResponseEntity<Map<String, Boolean>> location(@RequestBody Location loc) {
        System.out.println("[Location Event] vehicleID: " + loc.getVehicleID() +
                ", stopID: " + loc.getStopID() +
                ", status: " + loc.getStatus());

        DailySchedules ds = dailyScheduleRepository.findByIsHolidayAndOrganizationIdAndVehicleId(dailyScheduleService.isWeekend(), loc.getOrgID(), loc.getVehicleID());


        ds.setCurrentStop(loc.getStopID());
        ds.setRouteStatus(DailySchedules.RouteStatus.valueOf(loc.getStatus().toUpperCase()));
        dailyScheduleRepository.save(ds);


        return ResponseEntity.ok(Map.of("ok", true));
    }


    // 노선 시작/종료 이벤트
    /*
    // 시작 시 첫번째 노선 자동 할당
    // 이 후 갱신하다가
    // 종료 요청이 들어오면 다음 노선이 있나 확인하고
    // 없으면 코스 종료
     */
    @PostMapping("/bus/route/start")
    public ResponseEntity<Map<String, Boolean>> routeStart(@RequestBody RouteReport data) {
        System.out.print("[Start Drive] vehicleID: " + data.getVehicleID() +
                ", routeID: " + data.getRouteID() + ", flag: ");
        System.out.println(data.isFlag() ? "Start" : "Terminate");
        System.out.println("data: " + data);
        DailySchedules ds = dailyScheduleRepository.findByIsHolidayAndOrganizationIdAndCourseId(dailyScheduleService.isWeekend(), data.getOrgID(), data.getCourseID());


        if(data.isFlag()) {
            System.out.println(data.getCourseID());
            ds.setCurrentRoute(data.getRouteID());
            ds.setVehicleId(data.getVehicleID());
            ds.setStatus(DailySchedules.Status.IN_SERVICE);
            ds.setRouteStatus(DailySchedules.RouteStatus.READY);
            dailyScheduleRepository.save(ds);
        }
        else{

            // Todo: 이 루트가 마지막 노선인지 확인하고 마지막 이면 Terminate아니면 Ready로 변경
            ds.setStatus(DailySchedules.Status.READY);
            ds.setCurrentStop(0);
            dailyScheduleRepository.save(ds);
        }

        return ResponseEntity.ok(Map.of("ok", true));
    }


    private Object readJson(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        return mapper.readValue(content, Object.class);
    }
}
