package com.ehdndqls.shuttle.realtimefleet;

import com.ehdndqls.shuttle.busstop.BusStops;
import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor

public class RealTimeController {
    private final RealTimeRepository realTimeRepository;
    private final BusStopsRepository busStopsRepository;
    // private RealTimeService realTimeService;

    @GetMapping("/real-time/update")
    public String update(@RequestParam Long dailyScheduleId,
                         @RequestParam Long stopId,
                         @RequestParam String status,
                         Model model) {
        updateService(dailyScheduleId, stopId, status);
        return "redirect:/";
    }

    @PostMapping("/real-time/update")
    @Transactional
    public ResponseEntity<String> updateService(@RequestParam Long dailyScheduleId,
                                                @RequestParam Long stopId,
                                                @RequestParam String status) {
        System.out.println("시작");
        // 1. 실시간 운행 정보 조회
        Optional<RealTimeFleet> optional = realTimeRepository.findByDailyScheduleId(dailyScheduleId);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 스케줄 ID에 대한 실시간 정보가 존재하지 않습니다.");
        }

        RealTimeFleet fleet = optional.get();

        System.out.println("피트: "+ fleet);
        // 2. 상태 값 검증 및 변환
        RealTimeFleet.FleetStatus fleetStatus;
        try {
            fleetStatus = RealTimeFleet.FleetStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("유효하지 않은 status 값입니다. (APPROACH / ARRIVAL / DEPARTURE)");
        }
        System.out.println("상태" +fleetStatus);

        // 3. 정류소 이름 조회
        Optional<String> stopName = busStopsRepository.findNameById(stopId);
        if (stopName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 stopId에 대한 정류소 이름을 찾을 수 없습니다.");
        }
        System.out.println("정류소: "+stopName);

        // 4. 정보 업데이트
        fleet.setStatus(fleetStatus);
        fleet.setCurrentStop(stopName.get());
        System.out.println("마지막" +fleet);
        realTimeRepository.save(fleet);

        return ResponseEntity.ok("실시간 운행 정보가 성공적으로 업데이트되었습니다.");
    }

    @GetMapping("/")
    String start(Authentication auth, Model model) {
        if(auth != null && auth.isAuthenticated()){
            List<RealTimeFleet> realTimeList;
            realTimeList = realTimeRepository.findAll();
            model.addAttribute("realTimeList", realTimeList);
            System.out.println(realTimeList);
            return "main.html";
        }
        else
            return "login.html";
    }
}
