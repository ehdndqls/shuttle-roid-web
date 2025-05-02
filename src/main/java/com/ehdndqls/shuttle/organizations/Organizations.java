package com.ehdndqls.shuttle.organizations;

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
    private Long organizationId;

    @Column(unique = true)
    private String organizationName;
    private String organizationPassword;

}
