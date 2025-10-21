package com.ehdndqls.shuttle.user;

import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.routes.RouteId;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

   // private final OrganizationsRepository organizationsRepository;
    private final UserService userService;
    private final RoutesRepository routesRepository;

    @GetMapping("/user/init")
    public String init(Model model) {
        model.addAttribute("orgs", userService.getOrganizations());
        return "user-init.html";
    }

    @GetMapping("/user")
    public String user(Model model, @RequestParam int orgID) {
        model.addAttribute("routeList", routesRepository.findById_OrganizationId(orgID));
        return "user-main.html";
    }

    @GetMapping("/user/route-detail")
    public String routeDetail(Model model, @RequestParam int orgId, @RequestParam int routeId) {
        Optional<Routes> optionalRoute = routesRepository.findById(new RouteId(orgId, routeId));
        model.addAttribute("route", optionalRoute.orElse(null));
        model.addAttribute("status", userService.getCurrentStops(orgId, routeId));

        return "user-route-detail.html";
    }
}
