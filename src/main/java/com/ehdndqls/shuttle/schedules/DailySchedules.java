package com.ehdndqls.shuttle.schedules;

import com.ehdndqls.shuttle.courses.Routes;
import com.ehdndqls.shuttle.drivers.Drivers;
import com.ehdndqls.shuttle.organizations.Organizations;
import com.ehdndqls.shuttle.vehicles.Vehicles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
public class DailySchedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyScheduleId;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (DailySchedules N : 1 Organizations)
    @JoinColumn(name = "organizationId", nullable = false) // 외래 키 이름 지정
    private Organizations organization;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (DailySchedules N : 1 Route)
    @JoinColumn(name = "routeId", nullable = false) // 외래 키 이름 지정
    private Routes route;

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 관계 설정 (DailySchedules N : 1 Drivers)
    @JoinColumn(name = "driverId", nullable = false) // 외래 키 이름 지정
    private Drivers driver;

    @ManyToOne(fetch = FetchType.LAZY) //  다대일 관계 설정 (DailySchedules N : 1 Vehicles)
    @JoinColumn(name = "vehcicleId", nullable = false) // 외래 키 이름 지정
    private Vehicles vehicle;

    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    public enum ScheduleStatus {
        READY,      // 운행준비
        RUNNING,    // 운행중
        COMPLETED   // 운행완료
    }
}
