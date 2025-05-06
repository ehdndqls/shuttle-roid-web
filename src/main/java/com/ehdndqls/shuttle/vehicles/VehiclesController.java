package com.ehdndqls.shuttle.vehicles;

import com.ehdndqls.shuttle.dto.VehicleForm;
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
public class VehiclesController {

    private final VehiclesRepository vehiclesRepository;
    private final VehiclesService vehiclesService;
    private final OrganizationsService organizationsService;


    @GetMapping("/vehicle")
    public String redirectDriver(){
        return "redirect:/vehicle/page/1";
    }

    @GetMapping("/vehicle/page/{page}")
    public String vehicle(Authentication auth, Model model, @PathVariable Integer page) {
        Long id = organizationsService.getOrganizationId(auth);

        if (page == null || page < 1) {
            page = 1;
        }
        Page<Vehicles> vehicleList = vehiclesRepository.findByOrganizationId(id, PageRequest.of(page-1, 5));

        model.addAttribute("Vehicles", vehicleList);  // 드라이버 리스트
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("totalPages", vehicleList.getTotalPages()); // 총 페이지 수

        return "vehicle.html";
    }

    @GetMapping("/vehicle/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        vehiclesRepository.findById(id).ifPresent(vehicle -> model.addAttribute("vehicle", vehicle));
        return "modify-vehicle.html";
    }

    @PostMapping("/vehicle/modify")
    public String modifyDriver(@ModelAttribute VehicleForm vehicleForm, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        vehiclesService.modify(vehicleForm, id);
        System.out.println(vehicleForm);
        return "redirect:/vehicle/page/1";
    }

    @GetMapping("/vehicle/search")
    public String search(@RequestParam(required = false) String searchText,
                         @RequestParam(required = false) Vehicles.VehicleType vehicleType,
                         @RequestParam(required = false) Integer vehicleYear,
                         Model model, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        List<Vehicles> vehicleList = vehiclesService.search(searchText, vehicleType, vehicleYear ,id);
        model.addAttribute("Vehicles", vehicleList);
        model.addAttribute("currentPage", 1); // 현재 페이지
        model.addAttribute("totalPages", 1); // 총 페이지 수
        return "vehicle.html";
    }

    @DeleteMapping("/vehicle/delete/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable Long id) {
        vehiclesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
