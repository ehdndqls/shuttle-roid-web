package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class DailySchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    // 기관명
    private Integer organizationId;

    // 스케줄 날짜
    private LocalDate date;

    // 코스 엔티티 join 안하고 그냥 Integer 코스키만 저장
    @Column(nullable = false)
    private Integer courseId;

    // 차량Id
    private Integer vehicleId;

    // 승무원Id
    private Integer driverId;

    // 현재 정류소 정보
    private Integer currentRoute;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private RouteStatus routeStatus;

    public enum Status {
        READY,    // 운행 준비
        NORMAL,   // 정상
        DELAY,    // 지연
        TERMINATE // 운행종료
    }

    public enum RouteStatus {
        READY,      // 준비
        APPROACH,   // 접근
        ARRIVAL,    // 도착
        DEPARTURE,  // 출발
    }

    @PrePersist
    public void prePersist() {
        if (status == null) status = Status.valueOf("READY");
        if (currentRoute == null) currentRoute = 0;
        if (vehicleId == null) vehicleId = 0;
        if (driverId == null) driverId = 0;
    }

}
