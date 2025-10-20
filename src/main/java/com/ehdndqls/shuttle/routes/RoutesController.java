package com.ehdndqls.shuttle.routes;


import com.ehdndqls.shuttle.busstop.BusStopsRepository;
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
        //List<RouteResponseDto> routes = routesService.getRoutesForOrganization(id);
        model.addAttribute("routes", routesRepository.findById_OrganizationId(id));
        return "route.html";
    }



    @GetMapping("/routes/modify")
    public String modify(@RequestParam(value = "routeId", required = false) Integer routeId, Model model, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        if(routeId == null) {
            routeId = 0;
        }
        RouteId id = new RouteId(organizationId, routeId);
        routesRepository.findById(id).ifPresent(routes -> model.addAttribute("route", routes));
        model.addAttribute("stops", busStopsRepository.findById_OrganizationId(organizationId));
        return "modify-route.html";
    }

    @PostMapping("/routes/modify")
    public String modifyRoute(@ModelAttribute RouteDto routeForm, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        routesService.modify(routeForm, organizationId);
        return "redirect:/routes";
    }

    @GetMapping("/routes/search")
    public String search(@RequestParam(required = false) String searchText,
                         @RequestParam(required = false) Routes.RouteType routeType,
                         Model model, Authentication auth) {
        Integer id = organizationsService.getOrganizationId(auth);
        List<Routes> routes = routesService.search(searchText, routeType, id);
        model.addAttribute("routes", routes);
        return "route.html";
    }

    @DeleteMapping("/routes/delete/{routeId}")
    public ResponseEntity<Object> deleteRoutes(@PathVariable Integer routeId, Authentication auth) {
        RouteId id = new RouteId(organizationsService.getOrganizationId(auth), routeId);
        routesRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
