package com.ehdndqls.shuttle.courses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Routes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;
    private String routeName;
    private Boolean holidayService;
    private String estimatedTime; // 예정 소요 시간
    @Column(nullable = false)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @Enumerated(EnumType.STRING)
    private VehicleType typeRestriction; // 차량 유형 제한

    @Convert(converter = StopsConverter.class)
    @Column(columnDefinition = "json")
    private List<Long> stops; // 정류소 ID 목록


    public enum RouteType {
        CIRCULATION,   // 순환 노선
        ONE_WAY,       // 편도 노선
        ROUND_TRIP     // 왕복 노선
    }

    public enum VehicleType {
        Standard,   // 일반
        Special,    // 특수
        Large   // 대형
    }
}
