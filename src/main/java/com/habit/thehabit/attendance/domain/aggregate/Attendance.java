package com.habit.thehabit.attendance.domain.aggregate;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Space;
import com.habit.thehabit.member.command.domain.aggregate.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_ATTENDANCE")
@TableGenerator(
        name = "SEQ_ATTENDANCE_ID",
        table = "TBL_SEQ_ATTENDANCE",
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
    @JoinColumn(name = "SPACE_ID")
    private Space space; //모임공간

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATTENDANCE_DATE")
    private Date attendanceDate; //출석일


}
