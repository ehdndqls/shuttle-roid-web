package com.ehdndqls.shuttle.dto;

import com.ehdndqls.shuttle.routes.Routes;
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

    private Integer routeId;
    private String routeNum;
    private String routeName;
    private Routes.RouteType routeType;
    private String stopList;
    private String estimatedTime;

    // 기본 생성자
    public RouteForm() {
    }

    // 모든 필드를 사용하는 생성자
    public RouteForm(Integer routeId, String routeNum, String routeName, Routes.RouteType routeType, String stopList, String estimatedTime) {
        this.routeId = routeId;
        this.routeNum = routeNum;
        this.routeName = routeName;
        this.routeType = routeType;
        this.stopList = stopList;
        this.estimatedTime = estimatedTime;
    }

    public List<Integer> getStopList() {
        if (stopList == null || stopList.isBlank()) return List.of();
        try {
            return Arrays.stream(stopList.replaceAll("\\[|\\]", "")  // 대괄호 제거
                            .split(","))                                     // 쉼표 기준 나눔
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());                   // List<Long>으로 변환
        } catch (Exception e) {
            throw new IllegalArgumentException("stops 파싱 실패: " + stopList, e);
        }
    }

}
