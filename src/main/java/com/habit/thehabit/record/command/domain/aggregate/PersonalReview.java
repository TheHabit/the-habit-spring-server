package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import com.habit.thehabit.record.command.domain.aggregate.embeddable.GradeLevel;

import javax.persistence.*;

@Entity
@DiscriminatorValue("P")
public class PersonalReview extends Record{

    @Column( name = "IMPRESSIVE_PAGE_IMG")
    private String impressivePageImg;

    @Enumerated(EnumType.STRING)
    private GradeLevel grade; //평점

    @Column(name = "CONTENT")
    private String Content; //독후감

    @Embedded
    private Period period; //독서기간
}
