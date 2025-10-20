package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@RequiredArgsConstructor
public class DailyScheduleService {
    private final DailyScheduleRepository dailyScheduleRepository;
    private final OrganizationsRepository organizationsRepository;

    public void GenerateSchedule(){
        LocalDate now = LocalDate.now();
        List<DailySchedules> todaySchedules = dailyScheduleRepository.findById_Date(now);
        DailySchedules newSchedule;

        List<Integer> organizationIds = organizationsRepository.findAllOrganizationIdBy();

        for(Integer oi : organizationIds){
            newSchedule = new DailySchedules();

        }
    }

}
