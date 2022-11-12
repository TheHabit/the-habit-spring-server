package com.habit.thehabit.attendance.command.app.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceInfoDTO {
    private LocalDateTime date;
    private int memberCode;
    private String memberName;
    private String percentage;

}
