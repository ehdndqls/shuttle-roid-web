package com.ehdndqls.shuttle.busstop;

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
    private Integer sensitivity;
    private Long organizationId;

    @PrePersist // 만약 sensitivity값이 없다면 자동으로 200으로 설정
    public void prePersist() {
        if (sensitivity == null) {
            sensitivity = 200;
        }
    }
    //private var notice;



}
