package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.record.command.domain.aggregate.embeddable.GradeLevel;

import javax.persistence.*;


@Entity
@DiscriminatorValue("C")
public class ClubReview extends Record {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @Column(name = "REVIEW_GRADE")
    @Enumerated(EnumType.STRING)
    private GradeLevel grade; //평점

    @Column(name = "CONTENT")
    private String content; //후기
}
