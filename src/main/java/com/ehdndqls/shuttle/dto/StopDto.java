package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.busstop.BusStopId;
import com.ehdndqls.shuttle.busstop.BusStops;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
// 노선 설정에서 사용되는 정류소 DTO
public class StopDto {
    private String id;
    private String name;
    private String details;

    public StopDto(BusStops stop) {
        this.id = stop.getId().getOrganizationId().toString() + "-" + stop.getId().getStopId().toString();
        this.name = stop.getStopName();
        //Todo: 여기에 시간정보 입력
        this.details = "시간관련내용 아직안함";   // 남은 시간 계산하여 삽입
    }
}
