package com.ehdndqls.shuttle.courses;

import com.ehdndqls.shuttle.routes.Routes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;


    @Column(nullable = false)
    private Long organizationId;

    private boolean HolidayService; // boolean 값은 default가 false

    private String typeRestriction;
}
