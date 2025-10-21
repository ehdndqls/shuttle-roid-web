package com.ehdndqls.shuttle.busserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoredData {
    private String receivedAt;
    private String sourceIp;
    private BusReport payload;
}
