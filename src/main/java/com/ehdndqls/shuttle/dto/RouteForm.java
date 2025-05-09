package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.courses.Routes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
public class RouteForm {

    private Long routeId;
    private String routeName;
    private Boolean holidayService;
    private Routes.RouteType routeType;
    private Routes.VehicleType typeRestriction; // 차량 유형 제한

  //  private List<Long> stops; // 정류소 ID 목록

    private String stopList;

    // 기본 생성자
    public RouteForm() {
    }

    // 모든 필드를 사용하는 생성자
    public RouteForm(Long routeId, String routeName, Boolean holidayService, Routes.RouteType routeType, Routes.VehicleType typeRestriction, String stopList) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.holidayService = holidayService;
        this.routeType = routeType;
        this.typeRestriction = typeRestriction;
        this.stopList = stopList;
    }

    public List<Long> getStops() {
        if (stopList == null || stopList.isBlank()) return List.of();
        try {
            return Arrays.stream(stopList.replaceAll("\\[|\\]", "")  // 대괄호 제거
                            .split(","))                                     // 쉼표 기준 나눔
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());                   // List<Long>으로 변환
        } catch (Exception e) {
            throw new IllegalArgumentException("stops 파싱 실패: " + stopList, e);
        }
    }

}
