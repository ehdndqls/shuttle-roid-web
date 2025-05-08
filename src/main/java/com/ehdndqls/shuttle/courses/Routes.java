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
    private Boolean HolidayService; // default > False
    private String TypeRestriction; // 차량 유형 제한
    @Column(nullable = false)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @ElementCollection
    @CollectionTable(name = "route_stops", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "stop_id")
    private List<Long> stops; // 정류소 ID 목록


    public enum RouteType {
        CIRCULATION,   // 순환 노선
        ONE_WAY,       // 편도 노선
        ROUND_TRIP     // 왕복 노선
    }
}
