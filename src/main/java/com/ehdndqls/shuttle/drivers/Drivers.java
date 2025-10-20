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
    private Integer driverId;   // PK

    @Column(nullable = false)
    private String driverName;  // 이름

    @Column(nullable = false)
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String employeeNumber; // 사원번호

    @Column(length = 6, nullable = false)
    private String ssnFront;    // 주민번호 앞자리 (생년월일 6자리)

    @Column(nullable = false)
    private Integer organizationId; // 기관코드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType; // 정규 / 비정규

    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.active = true;
    }

    public enum EmploymentType {
        Regular,   // 정규
        Contract   // 비정규
    }
}

