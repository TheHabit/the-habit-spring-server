package com.habit.thehabit.club.command.domain.aggregate.embeddable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class Period {
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
}
