package com.habit.thehabit.club.command.domain.aggregate.embeddable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

@Embeddable
@Data
public class Period {
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private LocalDateTime startDate;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    public Period(){}
    public Period(LocalDateTime startDate , LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
