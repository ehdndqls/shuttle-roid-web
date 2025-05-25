package com.ehdndqls.shuttle.schedules;

import com.ehdndqls.shuttle.realtimefleet.RealTimeFleet;
import com.ehdndqls.shuttle.realtimefleet.RealTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DailySchedulesController {
    private final DailySchedulesRepository dailySchedulesRepository;
    private final RealTimeRepository realTimeRepository;
    private final DailySchedulesService dailySchedulesService;



    @GetMapping("/schedule/start")
    public String startGet(@RequestParam Long scheduleId) {
        start(scheduleId);
        return "redirect:/";
    }

    @PostMapping("/schedule/start")
    public void start(@RequestParam Long scheduleId) {
        Optional<DailySchedules> optional = dailySchedulesRepository.findById(scheduleId);

        if (optional.isPresent()) {
            DailySchedules schedule = optional.get();
            schedule.setStatus(DailySchedules.ScheduleStatus.RUNNING); // 운행중 상태로 변경
            dailySchedulesRepository.save(schedule); // 변경된 상태 저장
        } else {
            throw new IllegalArgumentException("해당 ID의 스케줄이 존재하지 않습니다: " + scheduleId);
        }
        RealTimeFleet fleet = new RealTimeFleet();
        fleet.setDailyScheduleId(scheduleId);
        fleet.setCurrentStop("운행시작");
        fleet.setStatus(RealTimeFleet.FleetStatus.DEPARTURE);

        realTimeRepository.save(fleet);

    }

    @PostMapping("/schedule/complete")
    public void complete(@RequestParam Long scheduleId) {
        Optional<DailySchedules> optional = dailySchedulesRepository.findByDailyScheduleId(scheduleId);
        if (optional.isPresent()) {
            DailySchedules schedule = optional.get();
            schedule.setStatus(DailySchedules.ScheduleStatus.COMPLETED);
            dailySchedulesRepository.save(schedule);
        }

        Optional<RealTimeFleet> optional2 = realTimeRepository.findById(scheduleId);
        optional2.ifPresent(realTimeRepository::delete);

    }
}
