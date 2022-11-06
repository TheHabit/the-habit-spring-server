package com.habit.thehabit.record.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingPeriod {
    private Date startDate;
    private Date endDate;
}
