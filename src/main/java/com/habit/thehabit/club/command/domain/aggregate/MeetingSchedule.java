package com.habit.thehabit.club.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_MEETING_SCHEDULE")
@TableGenerator(
        name = "SEQ_MEETING_SCHEDULE_ID",
        table = "MY_SEQUENCES",
        pkColumnValue = "MEETING_SCHEDULE_SEQ",
        allocationSize = 1
)
public class MeetingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_MEETING_SCHEDULE_ID"
    )
    @Column(name = "MEETING_SCHEDULE_ID")
    private int id;

    @Column(name = "MEETING_DATE_TIME")
    private LocalDateTime meetingDateTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CLUB_ID")
    private Club club;


    public void setClub(Club club){
        this.club = club;
        if(!club.getMeetingScheduleList().contains(this)){
            club.getMeetingScheduleList().add(this);
        }
    }
}
