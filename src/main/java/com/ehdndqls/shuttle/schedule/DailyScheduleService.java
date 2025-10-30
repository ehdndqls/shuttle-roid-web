package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.courses.CourseRepository;
import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.schedule.dto.DailyScheduleForm;
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
        DailySchedules dailySchedules;
        List<DailySchedules> dailyScheduleList = dailyScheduleRepository.findByDateAndOrganizationId(today, organizationId);

        if(dailyScheduleList != null){
           for(DailySchedules dailySchedule : dailyScheduleList){
               //Todo: 여기서 데이터 넣기

           }
        }



        return dailyScheduleForm;
    }
}
