package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.routes.RouteId;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final RoutesRepository routesRepository;

    public void GenerateRouteDetails(List<Integer> routeIds, Integer organizationId) {
        RouteId id;
        Optional<Routes> route;
        for(Integer routeId : routeIds) {
            id = new RouteId(routeId, organizationId);
            route = routesRepository.findById(id);

        }
    }

    @Transactional
    public void modify(CourseDto courseForm, Integer organizationId){
       Courses course;
       Integer courseId = courseForm.getCourseId();

       if(courseId == null) {
           // 새로운 코스일경우 해당 기관에 존재하는 최대 코스 Id + 1을 키로 설정
           Integer maxId = courseRepository.findMaxCourseIdByOrganizationId(organizationId);
           courseId = (maxId == null ? 1 : maxId + 1);
       }

       CourseId id = new CourseId(organizationId, courseId);

       Optional<Courses> optCourse = courseRepository.findById(id);
       course = optCourse.orElseGet(Courses::new);

       course.setId(id);
       course.setCourseName(courseForm.getName());
       course.setRouteType(courseForm.getRouteType());
       course.setHolidayServiceAvailable(courseForm.getHolidayServiceAvailable());
       course.setRouteList(courseForm.getRouteList());
       courseRepository.save(course);
    }

    public List<RouteDetail> convertRoutesToDTO(Integer organizationId) {
        List<RouteDetail> routeDetails = new ArrayList<>();
        List<Routes> routes = routesRepository.findById_OrganizationId(organizationId);
        Integer routeId;
        String routeName;
        LocalTime startTime;
        Integer estimatedTime;
        for(Routes route : routes) {
            routeId = route.getId().getRouteId();
            routeName = route.getRouteName();
            startTime = null;
            estimatedTime = route.getEstimatedTime();
            routeDetails.add(new RouteDetail(routeId, routeName, startTime, estimatedTime));
        }
        return routeDetails;
    }

}
