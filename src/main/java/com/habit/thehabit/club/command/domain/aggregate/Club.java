package com.habit.thehabit.club.command.domain.aggregate;

import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Space;
import com.habit.thehabit.member.command.domain.aggregate.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_CLUB")
@TableGenerator(
        name = "SEQ_ClUB_ID",
        table = "TBL_SEQ_ClUB",
        pkColumnValue = "CLUB_SEQ",
        allocationSize = 1
)
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_ClUB_ID")
    @Column(name = "CLUB_ID")
    private int id;



    @Column(name = "CLUB_NAME")
    private String clubName; //모임명

    @Column(name = "BOOK_NAME")
    private String bookName; //대상도서명

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="startDate", column = @Column(name="RECRUIT_START_DATE")),
            @AttributeOverride(name="endDate", column = @Column(name="RECRUIT_END_DATE")),
    })
    private Period recruitPeriod; //모집 기간

    @Embedded
    private Period period; //클럽 활동 기간

    @Column(name = "NUMBEROF_MEMBER")
    private int numberOfMember; //모집인원

    @Enumerated(EnumType.STRING)
    private ClubStatus status; //진행상태

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE_ID")
    private Space space;
}
