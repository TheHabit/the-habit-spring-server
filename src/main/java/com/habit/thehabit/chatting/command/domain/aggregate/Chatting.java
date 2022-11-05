package com.habit.thehabit.chatting.command.domain.aggregate;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Space;
import com.habit.thehabit.member.command.domain.aggregate.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_CHATTING")
@TableGenerator(
        name = "SEQ_CHATTING_ID",
        table = "TBL_SEQ_CHATTING",
        pkColumnValue = "CHATTING_SEQ",
        allocationSize = 1
)
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_CHATTING_ID")
    @Column(name = "CHATTING_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE_ID")
    private Space space;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHATTING_TIME")
    private Date chattingTime;
}
