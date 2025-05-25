package com.ehdndqls.shuttle.busstop;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class EstimatedTime {

    @EmbeddedId
    private EstimatedTimeId id;

    private Integer duration;   // 초단위 소요시간
}
