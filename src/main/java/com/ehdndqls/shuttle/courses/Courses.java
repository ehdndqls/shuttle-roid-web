package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.organizations.Organizations;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (Courses N : 1 Route)
    @JoinColumn(name = "routeId", nullable = false) // 외래 키 이름 지정
    private Routes route;


    @Column(nullable = false)
    private Long organizationId;

    private boolean HolidayService; // boolean 값은 default가 false

    private String typeRestriction;
}
