package com.ehdndqls.shuttle.busserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginReq {
    private int orgID;
    private int driverID;
    private String password;
}
