/*
 * | 지선번호 코드 |
 * 0 -> 없음
 * 1 -> A
 * 2 -> B
 * 3 -> C
 * 4 -> -6
 * 5 -> -5
 * 6 -> -4
 * 7 -> -3
 * 8 -> -2
 * 9 -> -1
 *
 *  | 노선 종별 코드 |
 * 0 -> 상행
 * 1 -> 하행
 * 2 -> 순환
 * 3 -> 편도
 *
 * | ID 구성 방식 |
 * ID = 노선 번호 (1~4자리) + 지선 코드(1자리) + 노선 종별 (1자리)
 * -> 총 3~6자리의 숫자 조합으로 구성
 *
 * | 예시 |
 * 720-1번 상행 -> 720 + 9 + 0 = 72090
 * 110A번 순환 -> 110 + 1 + 2 = 11012
 */
package com.ehdndqls.shuttle.routes;

import com.ehdndqls.shuttle.busstop.RouteId;
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
    @EmbeddedId
    private RouteId id;   // 기본키 -> 복합키: organizationId + routeId
    private String routeNum;
    private String routeName;   // 노선 이름
    private String estimatedTime; // 예정 소요 시간

    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @Convert(converter = StopsConverter.class)
    @Column(columnDefinition = "json")
    private List<Integer> stopList; // 정류소 ID 목록


    @Getter
    public enum RouteType {
        ROUND_TRIP_UP(0),       // 왕복 노선(상행) - 0
        ROUND_TRIP_DOWN(1),     // 왕복 노선(하행) - 1
        CIRCULATION(2),         // 순환 노선 - 2
        ONE_WAY(3);             // 편도 노선 - 3

        private final int code;

        RouteType(int code){
            this.code = code;
        }

    }

    /*
    public enum VehicleType {
        Standard,   // 일반
        Special,    // 특수
        Large   // 대형
    }*/
}
