package com.habit.thehabit.attendance.command.app.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceInfoDTO {
    private Date date;
    private int memberCode;
    private String memberName;
}
