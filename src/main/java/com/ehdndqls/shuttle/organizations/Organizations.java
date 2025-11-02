package com.ehdndqls.shuttle.organizations;

import com.ehdndqls.shuttle.schedule.DailySchedules;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter

public class Organizations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organizationId;

    @Column(unique = true)
    private String organizationName;
    private String organizationPassword;

    private String updateVersion;

    @PrePersist
    public void prePersist() {
        if (updateVersion == null) { updateVersion = "2025110101"; }
    }
}
