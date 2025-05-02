package com.ehdndqls.shuttle.drivers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class Drivers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    @Column(nullable = false)
    private Long organizationId;

    private String driverName;
    private Integer joinYear;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private DriverType type;

    private Boolean active;
    @PrePersist
    public void prePersist() {
        this.active = true;
    }

    public enum DriverType {
        Standard,   // 일반
        Special,    // 특수
        Large   // 대형
    }

}
