package com.ehdndqls.shuttle.drivers;

import com.ehdndqls.shuttle.dto.DriverForm;
import com.ehdndqls.shuttle.organizations.OrganizationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class DriversController {
    private final DriversRepository driversRepository;
    private final DriversService driversService;
    private final OrganizationsService organizationsService;

    @GetMapping("/driver")
    public String redirectDriver(){
        return "redirect:/driver/page/1";
    }

    @GetMapping("/driver/page/{page}")
    public String driver(Authentication auth, Model model, @PathVariable Integer page) {

        Long id = organizationsService.getOrganizationId(auth);

        if (page == null || page < 1) {
            page = 1;
        }
        Page<Drivers> driverList = driversRepository.findByOrganizationId(id, PageRequest.of(page-1, 5));

        model.addAttribute("Drivers", driverList);  // 드라이버 리스트
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("totalPages", driverList.getTotalPages()); // 총 페이지 수

        return "driver.html";
    }

    @GetMapping("/driver/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        driversRepository.findById(id).ifPresent(driver -> model.addAttribute("driver", driver));
        return "modify-driver.html";
    }

    @PostMapping("/driver/modify")
    public String modifyDriver(@ModelAttribute DriverForm driverForm, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        driversService.modify(driverForm, id);
        System.out.println(driverForm);
        return "redirect:/driver/page/1";
    }

    @GetMapping("/driver/search")
    public String search(@RequestParam(required = false) String searchText,
                         @RequestParam(required = false) Drivers.DriverType type,
                         @RequestParam(required = false) Integer joinYear,
                         Model model, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        List<Drivers> driverList = driversService.search(searchText, type, joinYear ,id);
        model.addAttribute("Drivers", driverList);
        model.addAttribute("currentPage", 1); // 현재 페이지
        model.addAttribute("totalPages", 1); // 총 페이지 수
        return "driver.html";
    }

    @DeleteMapping("/driver/delete/{id}")
    public ResponseEntity<Object> deleteDriver(@PathVariable Long id) {
        driversRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
