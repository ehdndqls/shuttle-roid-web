package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.JsonConverter;
import com.ehdndqls.shuttle.routes.Routes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Courses {

    @EmbeddedId
    private CourseId id;

    // 코스 이름
    @Column(nullable = false)
    private String courseName;

    // 공휴일 운행 여부
    @Column(nullable = false)
    private Boolean holidayServiceAvailable;

    // 운행 제한 타입
    @Enumerated(EnumType.STRING)
    private OperationRestrictionType routeType;

    // 코스리스트
    @Convert(converter = RouteDetailListConverter.class)
    @Column(columnDefinition = "json")
    private List<RouteDetail> routeList;

    @Getter
    public enum OperationRestrictionType {
        NONE(0);    // 제한 없음 - 0

        private int code;

        OperationRestrictionType(int code) { this.code=code; }
    }


}
