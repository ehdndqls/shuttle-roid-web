package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.organizations.OrganizationsService;
import com.ehdndqls.shuttle.routes.RouteDto;
import com.ehdndqls.shuttle.routes.RouteId;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final OrganizationsService organizationsService;
    private final RoutesRepository routesRepository;


    @GetMapping("/course")
    public String redirectCourse(Model model) {
        return "redirect:/course/page/1";
    }

    @GetMapping("/course/page/{page}")
    public String course(Authentication auth, @PathVariable Integer page, Model model) {
        Integer organizationId = organizationsService.getOrganizationId(auth);

        if(page == null || page <1){
            page = 1;
        }

        Page<Courses> courseList = courseRepository.findById_OrganizationId(organizationId, PageRequest.of(page-1, 5));

        model.addAttribute("Courses", courseList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courseList.getTotalPages());

        return "course.html";
    }

    @GetMapping("/course/modify")
    public String modify(@RequestParam(value = "courseId", required = false) Integer courseId, Model model, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        if(courseId == null) {
            courseId = 0;
        }
        CourseId id = new CourseId(organizationId, courseId);
        courseRepository.findById(id).ifPresent(courses -> model.addAttribute("courses", courses));
        model.addAttribute("routes", courseService.convertRoutesToDTO(organizationId));
        return "modify-course.html";
    }

    @PostMapping("/course/modify")
    public String modifyRoute(@ModelAttribute CourseDto courseForm, Authentication auth) {
        Integer organizationId = organizationsService.getOrganizationId(auth);
        courseService.modify(courseForm, organizationId);
        return "redirect:/course";
    }


}
