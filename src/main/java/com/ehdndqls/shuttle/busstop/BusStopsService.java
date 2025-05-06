package com.ehdndqls.shuttle.busstop;

import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.dto.BusStopForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusStopsService {

    private final BusStopsRepository busStopsRepository;

    public BusStopsService(BusStopsRepository busStopsRepository) {
        this.busStopsRepository = busStopsRepository;
    }

    public void modify(BusStopForm busStopForm, Long organizationId) {
        BusStops busStop;
        // 신균지 중곤지 확인
        if(busStopForm.getStopId() != null) {
            busStop = busStopsRepository.findById(busStopForm.getStopId()).orElse(null);
        }
        else{
            busStop = new BusStops();
        }

        // 값 설정
        busStop.setStopName(busStopForm.getStopName());
        busStop.setLongitude(busStopForm.getLongitude());
        busStop.setLatitude(busStopForm.getLatitude());
        busStop.setSensitivity(busStopForm.getSensitivity());
        busStop.setOrganizationId(organizationId);

        // 저장
        busStopsRepository.save(busStop);
    }

    // 검색
    public List<BusStops> search(String searchText, Long organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return busStopsRepository.searchBusStops(searchText, organizationId);
    }
}
