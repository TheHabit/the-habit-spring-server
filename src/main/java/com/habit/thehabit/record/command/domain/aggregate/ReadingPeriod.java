package com.habit.thehabit.record.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingPeriod {
    @Column(name="START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "REPORT_DATE")
    private Date reportDate;
}
