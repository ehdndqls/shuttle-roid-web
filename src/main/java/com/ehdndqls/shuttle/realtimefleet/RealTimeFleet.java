package com.ehdndqls.shuttle.realtimefleet;

import com.ehdndqls.shuttle.schedules.DailySchedules;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class RealTimeFleet {

    @Id
    private Long dailyScheduleId;

    private String currentStop;

    @Enumerated(EnumType.STRING)
    private FleetStatus status;

    // 기본 생성자 (JPA 필수)
    public RealTimeFleet() {}

    public enum FleetStatus {
        APPROACH,  // 접근
        ARRIVAL,   // 도착
        DEPARTURE  // 출발
    }

}
