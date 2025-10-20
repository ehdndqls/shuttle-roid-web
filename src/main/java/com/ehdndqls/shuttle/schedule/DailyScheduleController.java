package com.ehdndqls.shuttle.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DailyScheduleController {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final DailyScheduleService dailyScheduleService;


    @GetMapping("/schedule")
    public String schedule(Model model) {

        return "schedule.html";
    }
}
