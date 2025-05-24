package com.ehdndqls.shuttle.realtimefleet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor

public class RealTimeController {
    // private RealTimeRepository realTimeRepository;
    // private RealTimeService realTimeService;

    @PostMapping("/real-time/start")
    public void startService(@RequestParam Long dailyScheduleId){
        // DB 에서 dailyScheduleId 로 검색해서 상태 운행중으로 변경
        // 성공시
    }

    @PostMapping("/real-time/update")
    public void updateService(@RequestParam Long dailyScheduleId, @RequestParam Long stopId, @RequestParam String status){
        // DB 에서 dailyScheduleId 로 검색해서 해당 정류소 status 변경
        // 성공시
        System.out.println(stopId);
    }

    @PostMapping("/real-time/end")
    public void endService(@RequestParam Long dailyScheduleId){
        // DB 에서 dailyScheduleId 로 검색해서 상태 운행중으로 변경
        // 성공시
    }


}
