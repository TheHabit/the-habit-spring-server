package com.habit.thehabit.club.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "DAY")
    private String day;

    @Temporal(TemporalType.TIME)
    @Column(name = "START_TIME")
    private Date startTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "END_TIME")
    private Date endTime;

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
}
