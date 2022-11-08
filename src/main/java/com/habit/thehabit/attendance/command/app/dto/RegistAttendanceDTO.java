package com.habit.thehabit.attendance.command.app.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RegistAttendanceDTO {
    private Date date;
    private int clubId;
    private int memberCode;
    private String clubName;
}
