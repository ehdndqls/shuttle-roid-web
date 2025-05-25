package com.ehdndqls.shuttle.busstop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class BusStops {

    @EmbeddedId
    private BusStopId id;   // 기본키 -> 복합키: stopId + organizationId

    // 정류소 이름
    @Column(nullable = false)
    private String stopName;

    // 위도
    @Column(nullable = false)
    private Double latitude;

    // 경도
    @Column(nullable = false)
    private Double longitude;

    // 접근, 도착, 출발 판정 거리
    @Column(nullable = false)
    private Integer approach;
    @Column(nullable = false)
    private Integer arrival;
    @Column(nullable = false)
    private Integer leave;

    @PrePersist // default approach -> 150 / arrival -> 30 / leave -> 50
    public void prePersist() {
        if (approach == null) {
            approach = 150;
        }
        if (arrival == null) {
            arrival = 30;
        }
        if (leave == null) {
            leave = 50;
        }
    }


}
