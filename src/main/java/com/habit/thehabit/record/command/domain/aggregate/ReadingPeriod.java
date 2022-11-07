package com.habit.thehabit.record.command.domain.aggregate;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@ToString
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
