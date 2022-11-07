package com.habit.thehabit.club.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_CLUB")
@TableGenerator(
        name = "SEQ_ClUB_ID",
        table = "MY_SEQUENCES",
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
    @Column(name = "STATUS")
    private ClubStatus status; //진행상태


    @OneToMany(mappedBy = "club" , cascade = CascadeType.ALL)
    private List<ClubMember> clubMemberList = new ArrayList<ClubMember>();

    @OneToMany(mappedBy = "club" , cascade = CascadeType.ALL)
    private List<Attendance> attendanceList = new ArrayList<Attendance>();

    public boolean isApply(){
        if(clubMemberList.size() < numberOfMember ){
            return true;
        }
        return false;
    }
    /*무한 루프에 빠지지 않도록 체크*/
    public void addClubMember(ClubMember clubMember){
        this.clubMemberList.add(clubMember);
        if(clubMember.getClub() != this){
            clubMember.setClub(this);
        }
    }
    public ClubDTO toClubDTO(){
        ClubDTO clubDTO = new ClubDTO(this.id, this.clubName, this.bookName, this.recruitPeriod.getStartDate(), this.recruitPeriod.getEndDate(),this.period.getStartDate(),this.period.getEndDate(),this.numberOfMember, this.status.toString());
        return clubDTO;
    }
}
