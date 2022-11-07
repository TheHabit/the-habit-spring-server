package com.habit.thehabit.club.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.habit.thehabit.club.command.app.dto.ClubMemberDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TBL_CLUB_MEMBER")
@TableGenerator(
        name = "SEQ_ClUBMEMBER_ID",
        table = "MY_SEQUENCES",
        pkColumnValue = "CLUBMEMBER_SEQ",
        allocationSize = 1
)
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
        generator = "SEQ_ClUBMEMBER_ID")
    @Column(name = "CLUB_MEMBER_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    public void setMember(Member member){
        this.member = member;
        //무한루프에 빠지지 않도록 체크
        if(!member.getClubMemberList().contains(this)){
            member.getClubMemberList().add(this);
        }
    }

    public void setClub(Club club){
        this.club = club;
        //무한루프에 빠지지 않도록 체크
        if(!club.getClubMemberList().contains(this)){
            club.getClubMemberList().add(this);
        }
    }

    }
