package com.habit.thehabit.club.command.domain.aggregate;

import com.habit.thehabit.member.command.domain.aggregate.Member;

import javax.persistence.*;

@Entity
@Table(name = "TBL_CLUBMEMBER")
@TableGenerator(
        name = "SEQ_ClUBMEMBER_ID",
        table = "TBL_SEQ_ClUBMEMBER",
        pkColumnValue = "CLUBMEMBER_SEQ",
        allocationSize = 1
)
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
        generator = "SEQ_ClUBMEMBER_ID")
    @Column(name = "CLUBMEMBER_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;
    }
