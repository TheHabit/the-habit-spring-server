package com.habit.thehabit.club.command.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.habit.thehabit.club.command.domain.aggregate.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class ScheduleDTO {
    private int scheduleid;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleDTO(int id, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.scheduleid = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

//    public void setStartTime(String startTime) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
//        this.startTime =formatter.parse(startTime);
//    }
//
//    public void setEndTime(String endTime) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
//        this.endTime = formatter.parse(endTime);
//    }
    public Schedule toSchedule(){
        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(this.dayOfWeek);
        schedule.setStartTime(this.startTime);
        schedule.setEndTime(this.endTime);
//        schedule.setStartTime(this.startTime);
//        schedule.setEndTime(this.endTime);
        return schedule;
    }


}
