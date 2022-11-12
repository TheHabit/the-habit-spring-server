package com.habit.thehabit.club.command.domain.aggregate.embeddable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
@Data
public class Period {
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    public Period(){}
    public Period(Date startDate , Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
