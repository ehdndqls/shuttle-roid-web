package com.ehdndqls.shuttle.schedule;

import com.ehdndqls.shuttle.courses.Courses;
import com.ehdndqls.shuttle.drivers.Drivers;
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

    // 기본키: organizationId + scheduleDate
    @EmbeddedId
    private DailyScheduleId id;

    // 운행 시작 시간
    private LocalTime startTime;

    // 코스 엔티티 join 안하고 그냥 Integer 코스키만 저장
    private Integer courseId;


    // 차량 엔티티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicles vehicle;

    // 승무원 엔티티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Drivers driver;

    private Integer currentRoute;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private Status status;

    public enum Status {
        READY,    // 운행 준비
        NORMAL,   // 정상
        DELAY,    // 지연
        TERMINATE // 운행종료
    }

    @PrePersist
    public void prePersist() {
        if (status == null) status = Status.valueOf("READY");
        if (currentRoute == null) currentRoute = 0;
    }

}
