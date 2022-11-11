package com.habit.thehabit.club.command.app.dto;

import com.habit.thehabit.club.command.domain.aggregate.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDTO {
    private int scheduleid;
    private String day;
    private Date startTime;
    private Date endTime;

    public ScheduleDTO(int id, String day, Date startTime, Date endTime) {
        this.scheduleid = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
        this.startTime =formatter.parse(startTime);
    }

    public void setEndTime(String endTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
        this.endTime = formatter.parse(endTime);
    }
    public Schedule toSchedule(){
        Schedule schedule = new Schedule();
        schedule.setDay(this.day);
        schedule.setStartTime(this.startTime);
        schedule.setEndTime(this.endTime);
        return schedule;
    }
}
