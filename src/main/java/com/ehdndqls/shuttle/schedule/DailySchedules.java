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

    // 주말/평일
    private Boolean isHoliday;

    // 코스 엔티티 join 안하고 그냥 Integer 코스키만 저장
    @Column(nullable = false)
    private Integer courseId;

    // 차량Id
    private Integer vehicleId;

    // 승무원Id
    private Integer driverId;

    // 현재 노선 정보
    private Integer currentRoute;

    // 현재 정류소 정보
    private Integer currentStop;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private RouteStatus routeStatus;

    public enum Status {
        READY,    // 운행 준비
        IN_SERVICE,   // 운행 중
        DELAY,    // 지연
        COMPLETE // 운행종료
    }

    public enum RouteStatus {
        READY,      // 준비
        APPROACH,   // 접근
        ARRIVAL,    // 도착
        DEPARTURE,  // 출발
    }

    @PrePersist
    public void prePersist() {
        if (status == null) status = Status.READY;
        if (routeStatus == null) routeStatus = RouteStatus.READY;
        if(currentStop == null) currentStop = 0;
        if (currentRoute == null) currentRoute = 0;
        if (vehicleId == null) vehicleId = 0;
        if (driverId == null) driverId = 0;
    }

}
