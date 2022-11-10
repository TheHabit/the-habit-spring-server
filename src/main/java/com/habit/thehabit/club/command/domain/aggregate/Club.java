package com.habit.thehabit.club.command.domain.aggregate;


import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;

import java.util.List;

@DynamicInsert
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

    @ColumnDefault("RECRUITING")
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ClubStatus status; //진행상태

    @OneToMany(mappedBy = "club" , cascade = CascadeType.ALL)
    private List<ClubMember> clubMemberList = new ArrayList<ClubMember>();

    @Column(name = "CURRENT_NUMBEROF_MEMBER")
    private int currentNumberOfMember;

    @OneToMany(mappedBy = "club" , cascade = CascadeType.ALL)
    private List<Attendance> attendanceList = new ArrayList<Attendance>();

    /* 2022-11-09 schedule 생성 */
    @OneToMany(mappedBy = "club" , cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<Schedule>();

    @Column(name = "IMAGE_URI")
    private String imageUri;

    @Column(name = "CLUB_INTRO")
    private String clubIntro;
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

    public void addAttandance(Attendance attendance){
        this.attendanceList.add(attendance);
        if(attendance.getClub()!=this){
            attendance.setClub(this);
        }
    }

    public void addSchedule(Schedule schedule){
        this.scheduleList.add(schedule);
        if(schedule.getClub() != this){
            schedule.setClub(this);
        }
    }

    /* currentNumberOfMember 카운팅 메소드*/
    public void addCurrentNumberOfMember(){
        this.currentNumberOfMember ++;
    }
    public void removeCurrentNumberOfMember(){
        this.currentNumberOfMember --;
    }
    public ClubDTO toClubDTO(){
        ClubDTO clubDTO = new ClubDTO(this.id,"", this.clubName, this.clubIntro
                ,this.bookName, this.recruitPeriod.getStartDate()
                ,this.recruitPeriod.getEndDate(),this.period.getStartDate()
                ,this.period.getEndDate()
                ,this.numberOfMember
                ,this.status.toString()
                ,this.imageUri);

        return clubDTO;
    }
}
