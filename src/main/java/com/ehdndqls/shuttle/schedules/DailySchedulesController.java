package com.ehdndqls.shuttle.schedules;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DailySchedulesController {
    private final DailySchedulesRepository dailySchedulesRepository;
    private final DailySchedulesService dailySchedulesService;

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
        // 여기서 RealTimeDB에 해당노선등록
    }

    @PostMapping("/schedule/complete")
    public void complete(@RequestParam Long scheduleId) {
        Optional<DailySchedules> optional = dailySchedulesRepository.findById(scheduleId);
        if (optional.isPresent()) {
            DailySchedules schedule = optional.get();
            schedule.setStatus(DailySchedules.ScheduleStatus.COMPLETED);
            dailySchedulesRepository.save(schedule);
            // 여기서 RealTimeDB에 해당노선삭제
        }
    }


}
