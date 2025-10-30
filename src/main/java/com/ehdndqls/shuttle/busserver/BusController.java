package com.ehdndqls.shuttle.busserver;

import com.ehdndqls.shuttle.busserver.dto.Location;
import com.ehdndqls.shuttle.busserver.dto.LoginReq;
import com.ehdndqls.shuttle.busserver.dto.OrgCheckReq;
import com.ehdndqls.shuttle.busserver.dto.RouteReport;
import com.ehdndqls.shuttle.schedule.DailyScheduleRepository;
import com.ehdndqls.shuttle.schedule.DailySchedules;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BusController {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final DailyScheduleRepository dailyScheduleRepository;

    //임시
    @PostMapping("/bus/org/check")
    public ResponseEntity<Map<String, String>> check(@RequestBody OrgCheckReq orgCheckReqReq) {

        // Todo: 로그인 부분 데이터 뽑아서 확인 후 반환

        return ResponseEntity.ok(Map.of("orgName", "헬로월드"));
    }

    // 로그인
    @PostMapping("/bus/auth/login")
    public  ResponseEntity<Map<String, String>> login(@RequestBody LoginReq req) {
        System.out.println("[Login Request] OrgID: " + req.getOrgID() + ", DriverID: " + req.getDriverID());
        // Todo: 이거 데이터 뽑아서 차량번호 보내기
        String vehicleNum = "경기12";

        return ResponseEntity.ok(Map.of("vehicleNum", vehicleNum));
    }

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

        DailySchedules ds = dailyScheduleRepository.findByDateAndOrganizationIdAndVehicleId(LocalDate.now(), loc.getOrgID(), loc.getVehicleID());


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
        DailySchedules ds = dailyScheduleRepository.findByDateAndOrganizationIdAndCourseId(LocalDate.now(), data.getOrgID(), data.getCourseID());


        if(data.isFlag()) {
            System.out.println(data.getCourseID());
            ds.setCurrentRoute(data.getRouteID());
            ds.setVehicleId(data.getVehicleID());
            ds.setStatus(DailySchedules.Status.NORMAL);
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
