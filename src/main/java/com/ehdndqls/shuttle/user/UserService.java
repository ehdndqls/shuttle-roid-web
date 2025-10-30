package com.ehdndqls.shuttle.user;

import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.organizations.OrganizationsRepository;
import com.ehdndqls.shuttle.schedule.DailyScheduleRepository;
import com.ehdndqls.shuttle.schedule.DailySchedules;
import com.ehdndqls.shuttle.user.dto.CurrentStop;
import com.ehdndqls.shuttle.user.dto.OrganizationData;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import com.ehdndqls.shuttle.vehicles.VehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OrganizationsRepository organizationsRepository;
    private final DailyScheduleRepository dailyScheduleRepository;
    private final VehiclesRepository vehiclesRepository;

    public List<OrganizationData> getOrganizations() {
        List<Organizations> orgs = organizationsRepository.findAll();
        List<OrganizationData> orgDataList = new ArrayList<>();
        for (Organizations org : orgs) {
            OrganizationData orgData = new OrganizationData(org.getOrganizationId(), org.getOrganizationName());
            orgDataList.add(orgData);
        }

        return orgDataList;
    }

    public List<CurrentStop> getCurrentStops(Integer orgId, Integer routeId) {
        List<DailySchedules> ds = dailyScheduleRepository.findByOrganizationIdAndCurrentRoute(orgId, routeId);
        List<CurrentStop> currentStops = new ArrayList<>();
        CurrentStop currentStop;
        for(DailySchedules d : ds) {
            currentStop = new CurrentStop();
            currentStop.setCurrentStop(d.getCurrentStop());
            currentStop.setStatus(d.getRouteStatus().toString());

            String vehicleNum = vehiclesRepository.findById(d.getVehicleId())
                    .map(Vehicles::getVehicleNumber)
                    .orElse("미등록 차량");

            currentStop.setVehicleNum(vehicleNum);
            currentStops.add(currentStop);
        }
        return currentStops;
    }
}
