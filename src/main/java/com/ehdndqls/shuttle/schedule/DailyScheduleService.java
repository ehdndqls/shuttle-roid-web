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
import com.ehdndqls.shuttle.schedule.dto.RealTimeBusOperationForm;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@RequiredArgsConstructor
public class DailyScheduleService {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final OrganizationsRepository organizationsRepository;
    private final CourseRepository courseRepository;
    private final VehiclesRepository vehiclesRepository;
    private final DriversRepository driversRepository;

    public void updateSchedule(){
        LocalDate now = LocalDate.now();
        List<Courses> courseList = courseRepository.findAll();
        DailySchedules schedule;

        for(Courses course : courseList){
            Optional<DailySchedules> optSchedule =
                    dailyScheduleRepository.findByCourseIdAndOrganizationId(
                            course.getId().getCourseId(),
                            course.getId().getOrganizationId()
                    );
            schedule = optSchedule.orElseGet(DailySchedules::new);

            schedule.setCourseId(course.getId().getCourseId());
            schedule.setOrganizationId(course.getId().getOrganizationId());
            schedule.setIsHoliday(course.getHolidayServiceAvailable());

            dailyScheduleRepository.save(schedule);
        }
    }

    public void modifySchedule(Integer scheduleId, Integer driverId, Integer vehicleId) {
        Optional<DailySchedules> OptSchedule = dailyScheduleRepository.findById(scheduleId);
        DailySchedules schedule;
        if(OptSchedule.isPresent()){
            schedule = OptSchedule.get();
            schedule.setDriverId(driverId);
            schedule.setVehicleId(vehicleId);
            dailyScheduleRepository.save(schedule);
        }
        else
            return;

    }

    public boolean isWeekend(){
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;
    }

    public List<DailyScheduleForm> GetDailySchedule(Integer organizationId){

        List<DailyScheduleForm> dailyScheduleForms = new ArrayList<>();
        List<DailySchedules> dailyScheduleList = dailyScheduleRepository.findByIsHolidayAndOrganizationId(isWeekend(), organizationId);


        if(dailyScheduleList != null){
            for(DailySchedules ds : dailyScheduleList){
                DailyScheduleForm dailyScheduleForm = new DailyScheduleForm();
                // 기본 데이터 (코스명, 차량번호, 운전기사명)
                dailyScheduleForm.setCourseNum(courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                        .map(Courses::getCourseName)
                        .orElse("코스명 검색실패"));
                dailyScheduleForm.setVehicleNum(vehiclesRepository.findById(ds.getVehicleId())
                        .map(Vehicles::getVehicleNumber)
                        .orElse("차량 미지정"));
                dailyScheduleForm.setDriverName(driversRepository.findById(ds.getDriverId())
                        .map(Drivers::getDriverName)
                        .orElse("기사 미지정"));
                dailyScheduleForm.setStatus(ds.getStatus());

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
                    dailyScheduleForm.setIsHoliday(course.getHolidayServiceAvailable());

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
                    dailyScheduleForm.setIsHoliday(false);
                }
                dailyScheduleForms.add(dailyScheduleForm);
            }
        }

        return dailyScheduleForms;
    }
public List<DailyScheduleForm> GetSchedule(Integer organizationId, boolean isHoliday){

        List<DailyScheduleForm> dailyScheduleForms = new ArrayList<>();
        List<DailySchedules> dailyScheduleList = dailyScheduleRepository.findByIsHolidayAndOrganizationId(isHoliday, organizationId);


        if(dailyScheduleList != null){
            for(DailySchedules ds : dailyScheduleList){
                DailyScheduleForm dailyScheduleForm = new DailyScheduleForm();
                // 기본 데이터 (코스명, 차량번호, 운전기사명)
                dailyScheduleForm.setCourseNum(courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                        .map(Courses::getCourseName)
                        .orElse("코스명 검색실패"));
                dailyScheduleForm.setVehicleNum(vehiclesRepository.findById(ds.getVehicleId())
                        .map(Vehicles::getVehicleNumber)
                        .orElse("차량 미지정"));
                dailyScheduleForm.setDriverName(driversRepository.findById(ds.getDriverId())
                        .map(Drivers::getDriverName)
                        .orElse("기사 미지정"));
                dailyScheduleForm.setStatus(ds.getStatus());

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
                    dailyScheduleForm.setIsHoliday(course.getHolidayServiceAvailable());

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
                    dailyScheduleForm.setIsHoliday(false);
                }
                dailyScheduleForms.add(dailyScheduleForm);
            }
        }

        return dailyScheduleForms;
    }

    public List<RealTimeBusOperationForm> GetRealTimeBusOperation(Integer organizationId){
        List<RealTimeBusOperationForm> realTimeBusOperationForms = new ArrayList<>();
        RealTimeBusOperationForm realTimeBusOperationForm = new RealTimeBusOperationForm();
// Todo: 리스트 뽑아서 리턴

        return realTimeBusOperationForms;

    }


}
