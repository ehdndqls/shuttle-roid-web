package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.courses.CourseId;
import com.ehdndqls.shuttle.courses.CourseRepository;
import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.courses.RouteDetail;
import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.drivers.DriversRepository;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.routes.RouteId;
import com.ehdndqls.shuttle.routes.Routes;
import com.ehdndqls.shuttle.routes.RoutesRepository;
import com.ehdndqls.shuttle.routes.StopDetail;
import com.ehdndqls.shuttle.schedule.dto.DailyScheduleForm;
import com.ehdndqls.shuttle.schedule.dto.DispatchScheduleDTO;
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
    private final RoutesRepository routesRepository;

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

    public void modifySchedule(DispatchScheduleDTO form) {
        Optional<DailySchedules> OptSchedule = dailyScheduleRepository.findByScheduleId(form.getScheduleId());
        DailySchedules schedule;
        if(OptSchedule.isPresent()){
            schedule = OptSchedule.get();
            schedule.setDriverId(form.getDriverId());
            schedule.setVehicleId(form.getVehicleId());
            System.out.println(form);
            System.out.println(schedule);
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
                // 기본 데이터 (코스명, 차량번호, 운전기사명, 스케줄 ID)
                dailyScheduleForm.setScheduleId(ds.getScheduleId());

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
                dailyScheduleForm.setScheduleId(ds.getScheduleId());

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

    public List<RealTimeBusOperationForm> GetRealTimeBusOperation(Integer organizationId) {
        List<RealTimeBusOperationForm> realTimeBusOperationForms = new ArrayList<>();

        // 현재 날짜 기준 스케줄 조회 (주말/평일 구분)
        List<DailySchedules> dailyScheduleList = dailyScheduleRepository.findByIsHolidayAndOrganizationId(isWeekend(), organizationId);

        if (dailyScheduleList != null) {
            for (DailySchedules ds : dailyScheduleList) {
                RealTimeBusOperationForm form = new RealTimeBusOperationForm();

                // 코스명
                String courseName = courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                        .map(Courses::getCourseName)
                        .orElse("코스명 검색실패");
                form.setCourseName(courseName);

                // 차량번호
                String vehicleNum = vehiclesRepository.findById(ds.getVehicleId())
                        .map(Vehicles::getVehicleNumber)
                        .orElse("차량 미지정");
                form.setVehicleNum(vehicleNum);

                // 기사명
                String driverName = driversRepository.findById(ds.getDriverId())
                        .map(Drivers::getDriverName)
                        .orElse("기사 미지정");
                form.setDriverName(driverName);

                // 코스의 정류장 리스트 (이전, 현재, 다음 정류소 추출)
                Courses course = courseRepository.findById(new CourseId(ds.getOrganizationId(), ds.getCourseId()))
                        .orElse(null);

                // 코스 상태 접근, 도착, 출발, 준비
                if (course != null && course.getRouteList() != null && !course.getRouteList().isEmpty()) {
                    List<RouteDetail> routeDetails = course.getRouteList();
                    int currentCourseRouteIndex = ds.getCurrentStopIndex(); // DailySchedules에 저장된 현재 경로 인덱스 가정

                    // 현재 코스에 포함된 Route 조회
                    if (currentCourseRouteIndex >= 0 && currentCourseRouteIndex < routeDetails.size()) {
                        RouteDetail currentRouteDetail = routeDetails.get(currentCourseRouteIndex);
                        form.setCurrentRoute(currentRouteDetail.getRouteName());

                        // Routes 테이블 접근
                        Optional<Routes> routeOpt = routesRepository.findById(new RouteId(organizationId, currentRouteDetail.getRouteId()));
                        if (routeOpt.isPresent()) {
                            Routes route = routeOpt.get();
                            List<StopDetail> stopList = route.getStopList();

                            // 정류소 인덱스 기반으로 이전/현재/다음 추출
                            int currentStopIndex = ds.getCurrentStopIndexInRoute(); // 현재 정류장 인덱스 가정
                            if (stopList != null && !stopList.isEmpty()) {
                                if (currentStopIndex >= 0 && currentStopIndex < stopList.size()) {
                                    form.setCurrentStop(stopList.get(currentStopIndex).getName());
                                } else {
                                    form.setCurrentStop("위치정보 없음");
                                }

                                if (currentStopIndex > 0) {
                                    form.setPreviousStop(stopList.get(currentStopIndex - 1).getName());
                                } else {
                                    form.setPreviousStop("출발지");
                                }

                                if (currentStopIndex < stopList.size() - 1) {
                                    form.setNextStop(stopList.get(currentStopIndex + 1).getName());
                                } else {
                                    form.setNextStop("종점");
                                }
                            }
                        } else {
                            // Route 조회 실패 시 기본값 처리
                            form.setCurrentStop("노선 없음");
                            form.setPreviousStop("");
                            form.setNextStop("");
                        }
                    } else {
                        form.setCurrentRoute("위치정보 없음");
                        form.setCurrentStop("위치정보 없음");
                        form.setPreviousStop("");
                        form.setNextStop("");
                    }
                }

                // 운행 상태
                form.setRouteStatus(ds.getRouteStatus());

                // 리스트에 추가
                realTimeBusOperationForms.add(form);
            }
        }

        return realTimeBusOperationForms;
    }



}
