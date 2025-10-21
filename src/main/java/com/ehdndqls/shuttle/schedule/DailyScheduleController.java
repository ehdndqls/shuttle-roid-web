package com.ehdndqls.shuttle.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DailyScheduleController {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final DailyScheduleService dailyScheduleService;


    @GetMapping("/schedule")
    public String schedule(Model model) {

        return "schedule.html";
    }

    @GetMapping("/testSchedule")
    public String testSchedule() {
        dailyScheduleService.GenerateSchedule();

        return "테스트 실행 완료!";
    }




}
