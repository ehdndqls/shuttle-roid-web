package com.ehdndqls.shuttle.busstop;

import com.ehdndqls.shuttle.dto.BusStopForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusStopsService {

    private final BusStopsRepository busStopsRepository;

    public void modify(BusStopForm busStopForm, Integer organizationId) {
        BusStops busStop;
        BusStopId id = null;
        String stopName;

        // 수정 요청
        if(busStopForm.getStopId() != null) {
            id = new BusStopId(organizationId, busStopForm.getStopId());
            busStop = busStopsRepository.findById(id).orElse(null);
        }
        // 신규 등록 요청
        else{
            busStop = new BusStops();

            // ID 생성 via(경유)일 경우 9000 <= stopId , normal 일 경우 stopId < 9000
            Integer stopId;
            if(busStopForm.getVia()) stopId = getNextNormalStopId(organizationId);
            else stopId = getNextViaStopId(organizationId);

            id = new BusStopId(organizationId, stopId);
            busStop.setId(id);
        }


        // 값 설정
        stopName = busStopForm.getStopName();
        if(!stopName.startsWith("(경유)") && busStopForm.getVia()){
            stopName = "(경유)" + stopName;
        }
        busStop.setStopName(stopName);
        busStop.setLongitude(busStopForm.getLongitude());
        busStop.setLatitude(busStopForm.getLatitude());
        busStop.setApproach(busStopForm.getApproach());
        busStop.setArrival(busStopForm.getArrival());
        busStop.setDeparture(busStopForm.getDeparture());

        // 저장
        busStopsRepository.save(busStop);
    }

    // 검색
    public List<BusStops> search(String searchText, Integer organizationId) {
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }
        return busStopsRepository.searchBusStops(searchText, organizationId);
    }

    // 각 업체 별 최대 ID+1을 반환해줌
    public Integer getNextNormalStopId(Integer organizationId) {
        return busStopsRepository.findMaxNormalStopIdByOrganizationId(organizationId).map(maxId -> maxId + 1).orElse(1);
    }
    public Integer getNextViaStopId(Integer organizationId) {
        return busStopsRepository.findMaxViaStopIdByOrganizationId(organizationId).map(maxId -> maxId + 1).orElse(1);
    }
}
