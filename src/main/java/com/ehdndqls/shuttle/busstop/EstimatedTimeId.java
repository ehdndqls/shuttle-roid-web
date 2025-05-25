package com.ehdndqls.shuttle.busstop;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data   // Getter, Setter, ToString, Equals, HashCode
@NoArgsConstructor  // JPA에서 사용하기 위한 tag
@AllArgsConstructor
public class EstimatedTimeId implements Serializable {
    private BusStopId departureStop;
    private BusStopId arrivalStop;
}
