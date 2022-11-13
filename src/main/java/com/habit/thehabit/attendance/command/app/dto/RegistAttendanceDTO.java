package com.habit.thehabit.attendance.command.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RegistAttendanceDTO {
    private LocalDateTime attendancedDate;
    private int clubId;
    private int memberCode;
    private String clubName;
}
