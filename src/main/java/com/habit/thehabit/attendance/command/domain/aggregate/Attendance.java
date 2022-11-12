package com.habit.thehabit.attendance.command.domain.aggregate;

import com.habit.thehabit.attendance.command.app.dto.AttendanceInfoDTO;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TBL_ATTENDANCE")
@TableGenerator(
        name = "SEQ_ATTENDANCE_ID",
        table = "MY_SEQUENCES",
        pkColumnValue = "ATTENDANCE_SEQ",
        allocationSize = 1
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_ATTENDANCE_ID")
    @Column(name = "ATTENDANCE_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_CODE")
    private Member member; //회원정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club; // 모임정보

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATTENDANCE_DATE")
    private LocalDateTime attendanceDate; //출석일

    @Column(name = "MEETING_DATE_TIME")
    private LocalDateTime meetingDateTime;

    public void setMember(Member member){
        this.member = member;
        //무한루프에 빠지지 않도록 체크
        if(!member.getAttendanceList().add(this)){
            member.getAttendanceList().add(this);
        }
    }

    public void setClub(Club club){
        this.club = club;
        //무한루프에 빠지지 않도록 체크
        if(!club.getAttendanceList().contains(this)){
            club.getAttendanceList().add(this);
        }
    }
    public AttendanceInfoDTO toDTO(){
        AttendanceInfoDTO attendanceInfoDTO = new AttendanceInfoDTO(this.attendanceDate, this.member.getMemberCode(), this.member.getName(),null);
        return attendanceInfoDTO;
    }
}
