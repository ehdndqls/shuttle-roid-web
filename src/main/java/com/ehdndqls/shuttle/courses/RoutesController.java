package com.ehdndqls.shuttle.courses;


import com.ehdndqls.shuttle.dto.RouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoutesController {

    private final RoutesRepository routesRepository;
    private final RoutesService routesService;

    @GetMapping("/routes")
    public String getRoutes(Model model) {
        List<RouteResponseDto> routes = routesService.getRoutesForOrganization(1L);
        model.addAttribute("routes", routes);
        return "route.html";
    }



}
