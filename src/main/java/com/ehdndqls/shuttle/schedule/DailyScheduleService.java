package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.courses.CourseId;
import com.ehdndqls.shuttle.courses.CourseRepository;
import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.courses.RouteDetail;
import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.drivers.DriversRepository;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.schedule.dto.DailyScheduleForm;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@RequiredArgsConstructor
public class DailyScheduleService {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final OrganizationsRepository organizationsRepository;
    private final CourseRepository courseRepository;
    private final VehiclesRepository vehiclesRepository;
    private final DriversRepository driversRepository;

    public void GenerateSchedule(){
        LocalDate now = LocalDate.now();
        DailySchedules newSchedule;

        List<Integer> organizationIds = organizationsRepository.findAllOrganizationIdBy();
        List<Courses> courseList;


        for(Integer oi : organizationIds){

            // 주말인지 확인
            if(isWeekend())
                courseList = courseRepository.findByOrganizationIdAndIsHoliday(oi, true);
            else
                courseList = courseRepository.findByOrganizationIdAndIsHoliday(oi, false);

            for(Courses course : courseList){
                newSchedule = new DailySchedules();
                newSchedule.setDate(LocalDate.now().plusWeeks(1));
                newSchedule.setOrganizationId(oi);
                newSchedule.setCourseId(course.getId().getCourseId());
                DailySchedules todaySchedules = dailyScheduleRepository.findByDateAndOrganizationIdAndCourseId(now, oi, course.getId().getCourseId());

                if(todaySchedules != null){
                    newSchedule.setDriverId(todaySchedules.getDriverId());
                    newSchedule.setVehicleId(todaySchedules.getVehicleId());
                }

                dailyScheduleRepository.save(newSchedule);
            }

        }
    }

    public boolean isWeekend(){
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;
    }

    public DailyScheduleForm GetDailySchedule(Integer organizationId){
        LocalDate today = LocalDate.now();
        DailyScheduleForm dailyScheduleForm = new DailyScheduleForm();
        List<DailySchedules> dailyScheduleList = dailyScheduleRepository.findByDateAndOrganizationId(today, organizationId);

        if(dailyScheduleList != null){
           for(DailySchedules ds : dailyScheduleList){
               // 기본 데이터 (코스명, 차량번호, 운전기사명)
               dailyScheduleForm.setCourseNum(courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                       .map(Courses::getCourseName)
                       .orElse("코스명 검색실패"));
               dailyScheduleForm.setVehicleNum(vehiclesRepository.findById(ds.getVehicleId())
                       .map(Vehicles::getVehicleNumber)
                       .orElse("미등록 차량"));
               dailyScheduleForm.setDriverName(driversRepository.findById(ds.getDriverId())
                       .map(Drivers::getDriverName)
                       .orElse("홍길동"));

               // 코스 정보에서 루트 리스트 꺼내기
               Courses course = courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                       .orElse(null);

               if (course != null && course.getRouteList() != null && !course.getRouteList().isEmpty()) {
                   List<RouteDetail> routes = course.getRouteList();

                   RouteDetail start = routes.get(0);
                   RouteDetail end = routes.get(routes.size() - 1);

                   dailyScheduleForm.setStartRoute(start.getRouteName());
                   dailyScheduleForm.setEndRoute(end.getRouteName());
                   dailyScheduleForm.setStartTime(start.getStartTime());

                   if (end.getStartTime() != null && end.getEstimatedTime() != null) {
                       dailyScheduleForm.setEndTime(end.getStartTime().plusMinutes(end.getEstimatedTime()));
                   } else {
                       dailyScheduleForm.setEndTime(null);
                   }
               } else {
                   dailyScheduleForm.setStartRoute("");
                   dailyScheduleForm.setEndRoute("");
                   dailyScheduleForm.setStartTime(null);
                   dailyScheduleForm.setEndTime(null);
               }
           }
        }

        return dailyScheduleForm;
    }


}
