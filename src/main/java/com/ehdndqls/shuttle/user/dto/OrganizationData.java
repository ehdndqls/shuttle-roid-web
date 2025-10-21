package com.ehdndqls.shuttle.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class OrganizationData {
    private Integer orgID;
    private String name;

    public OrganizationData(Integer orgID, String name) {
        this.orgID = orgID;
        this.name = name;
    }
}
