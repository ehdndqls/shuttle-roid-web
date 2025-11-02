package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.drivers.DriversRepository;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.organizations.OrganizationsService;
import com.ehdndqls.shuttle.schedule.dto.DailyScheduleForm;
import com.ehdndqls.shuttle.schedule.dto.DispatchScheduleDTO;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DailyScheduleController {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final DailyScheduleService dailyScheduleService;
    private final OrganizationsService organizationsService;
    private final VehiclesRepository vehiclesRepository;
    private final DriversRepository driversRepository;


    @GetMapping("/")
    public String Home(Model model, Authentication auth) {
        model.addAttribute("DailySchedules", dailyScheduleService.GetDailySchedule(organizationsService.getOrganizationId(auth)));
        model.addAttribute("RealTimeOpr", dailyScheduleService.GetRealTimeBusOperation(organizationsService.getOrganizationId(auth)));
        System.out.println("DailySchedules"+dailyScheduleService.GetDailySchedule(organizationsService.getOrganizationId(auth)));
        System.out.println("RealTimeOpr"+dailyScheduleService.GetRealTimeBusOperation(organizationsService.getOrganizationId(auth)));

        return "main.html";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Authentication auth) {
        model.addAttribute("Schedules", dailyScheduleService.GetSchedule(organizationsService.getOrganizationId(auth), false));
        model.addAttribute("HolidaySchedules", dailyScheduleService.GetSchedule(organizationsService.getOrganizationId(auth), true));
        model.addAttribute("Vehicles", vehiclesRepository.findByOrganizationId(organizationsService.getOrganizationId(auth)));
        model.addAttribute("Drivers", driversRepository.findByOrganizationId(organizationsService.getOrganizationId(auth)));
        return "schedule.html";
    }

    @PostMapping("/schedule/modify")
    @ResponseBody
    public Map<String, Object> modifySchedule(@RequestBody DispatchScheduleDTO form, Authentication auth) {
        System.out.println("post요청: " + form);
        dailyScheduleService.modifySchedule(form);
        return Map.of("status", "success"); // JSON 반환
    }


    @GetMapping("/testSchedule")
    public String testSchedule() {
        dailyScheduleService.updateSchedule();
        return "redirect:/";
    }


}
