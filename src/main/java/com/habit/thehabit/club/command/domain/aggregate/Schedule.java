package com.habit.thehabit.club.command.domain.aggregate;

import com.habit.thehabit.club.command.app.dto.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_SCHEDULE")
@TableGenerator(
        name = "SEQ_SCHEDULE_ID",
        table = "MY_SEQUENCES",
        pkColumnValue = "SCHEDULE_SEQ",
        allocationSize = 1
)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_SCHEDULE_ID"
    )
    @Column(name = "SCHEDULE_ID")
    private int id;

    @Column(name = "DAY_OF_WEEK")
    private String dayOfWeek;

//    @Temporal(TemporalType.TIME)
    @Column(name = "START_TIME")
    private LocalTime startTime;

//    @Temporal(TemporalType.TIME)
    @Column(name = "END_TIME")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    public void setClub(Club club){
        this.club = club;
        // 무한루프에 빠지지 않도록 체크
        if(!club.getScheduleList().contains(this)){
            club.getScheduleList().add(this);
        }
    }
    public ScheduleDTO toScheduleDTO(){
        ScheduleDTO scheduleDTO = new ScheduleDTO(this.id,this.dayOfWeek,this.startTime,this.endTime);
        return scheduleDTO;
    }
}
