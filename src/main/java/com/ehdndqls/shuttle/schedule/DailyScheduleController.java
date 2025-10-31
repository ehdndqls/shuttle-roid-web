package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.organizations.OrganizationsService;
import com.ehdndqls.shuttle.schedule.dto.DailyScheduleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DailyScheduleController {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final DailyScheduleService dailyScheduleService;
    private final OrganizationsService organizationsService;


    @GetMapping("/schedule")
    public String schedule(Model model, Authentication auth) {
        model.addAttribute("Schedules", dailyScheduleService.GetSchedule(organizationsService.getOrganizationId(auth), false));
        model.addAttribute("HolidaySchedules", dailyScheduleService.GetSchedule(organizationsService.getOrganizationId(auth), true));

        return "schedule.html";
    }

    @PostMapping("/schedule/modify")
    public String modifySchedule(@ModelAttribute Integer newDriverId,
                                 @ModelAttribute Integer newVehicleNum,
                                 @ModelAttribute Integer scheduleId) {
        dailyScheduleService.modifySchedule(newDriverId, newVehicleNum, scheduleId);
        return "redirect:/schedule";
    }


    @GetMapping("/testSchedule")
    public String testSchedule() {
        dailyScheduleService.updateSchedule();

        return "redirect:/";
    }


}
