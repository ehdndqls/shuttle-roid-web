package com.ehdndqls.shuttle.busstop;


import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.dto.BusStopForm;
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

public class BusStopsController {
    private final BusStopsRepository busStopsRepository;
    private final BusStopsService busStopsService;
    private final OrganizationsService organizationsService;

    @GetMapping("/busstop")
    public String redirectDriver(){
        return "redirect:/busstop/page/1";
    }

    @GetMapping("/busstop/page/{page}")
    public String busStop(Authentication auth, Model model, @PathVariable Integer page) {

        Long id = organizationsService.getOrganizationId(auth);

        if (page == null || page < 1) {
            page = 1;
        }
        Page<BusStops> busStopList = busStopsRepository.findByOrganizationId(id, PageRequest.of(page-1, 5));

        model.addAttribute("BusStops", busStopList);  // 정류소 리스트
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("totalPages", busStopList.getTotalPages()); // 총 페이지 수

        return "busstop.html";
    }
            // 여기 레파지토리에서 쿼리로 꺼내오는거

    @GetMapping("/busstop/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        busStopsRepository.findById(id).ifPresent(busStop -> model.addAttribute("busStop", busStop));
        return "modify-busstop.html";
    }

    @PostMapping("/busstop/modify")
    public String modifyBusStop(@ModelAttribute BusStopForm busStopForm, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        busStopsService.modify(busStopForm, id);
        return "redirect:/busstop/page/1";
    }

    @GetMapping("/busstop/search")
    public String search(@RequestParam(required = false) String searchText,
                         Model model, Authentication auth) {
        Long id = organizationsService.getOrganizationId(auth);
        List<BusStops> busStopList = busStopsService.search(searchText, id);
        model.addAttribute("BusStops", busStopList);
        model.addAttribute("currentPage", 1); // 현재 페이지
        model.addAttribute("totalPages", 1); // 총 페이지 수
        return "busstop.html";
    }

    @DeleteMapping("/busstop/delete/{id}")
    public ResponseEntity<Object> deleteBusStop(@PathVariable Long id) {
        busStopsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
