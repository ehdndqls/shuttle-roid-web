package com.ehdndqls.shuttle.routes;


import com.ehdndqls.shuttle.busstop.BusStopsRepository;
import com.ehdndqls.shuttle.busstop.RouteId;
import com.ehdndqls.shuttle.dto.RouteForm;
import com.ehdndqls.shuttle.dto.RouteResponseDto;
import com.ehdndqls.shuttle.organizations.OrganizationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoutesController {

    private final RoutesRepository routesRepository;
    private final RoutesService routesService;
    private final OrganizationsService organizationsService;
    private final BusStopsRepository busStopsRepository;

    @GetMapping("/routes")
    public String getRoutes(Model model, Authentication auth) {
        Integer id = organizationsService.getOrganizationId(auth);
        List<RouteResponseDto> routes = routesService.getRoutesForOrganization(id);
        model.addAttribute("routes", routes);
        return "route.html";
    }

    // Todo: 이게 지금 organizationId를 auth에서 뽑아서 정류소 리스트를 모델에 넣고
    // RouteId로 중복을 뽑는데 이거러 세부적으로 쓸건지는 html 수정하면서 확인하기
    @GetMapping("/routes/modify/{id}")
    public String modify(@PathVariable RouteId id, Model model, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        routesRepository.findById(id).ifPresent(routes -> model.addAttribute("route", routes));
        model.addAttribute("stops", busStopsRepository.findByOrganizationId(organizationId));
        return "modify-route.html";
    }

    @PostMapping("/routes/modify")
    public String modifyRoute(@ModelAttribute RouteForm routeForm, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        routesService.modify(routeForm, organizationId);
        return "redirect:/routes";
    }

    /*
    @GetMapping("/routes/search")
    public String search(@RequestParam(required = false) String searchText,
                         @RequestParam(required = false) Boolean holidayService,
                         @RequestParam(required = false) Routes.RouteType routeType,
                         Model model, Authentication auth) {
        Integer id = organizationsService.getOrganizationId(auth);
        List<RouteResponseDto> routes = routesService.search(searchText, holidayService, routeType, id);
        model.addAttribute("routes", routes);
        return "route.html";
    }
     */

    @DeleteMapping("/routes/delete/{id}")
    public ResponseEntity<Object> deleteRoutes(@PathVariable RouteId id) {
        routesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
