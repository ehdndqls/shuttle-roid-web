package com.ehdndqls.shuttle.courses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class BusStops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopId;
    private String stopName;
    private Double latitude;
    private Double longitude;
    private Integer radius;

    @PrePersist // 만약 radius값이 없다면 자동으로 200으로 설정
    public void prePersist() {
        if (radius == null) {
            radius = 200;
        }
    }
    //private var notice;



}
