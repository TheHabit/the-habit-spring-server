package com.habit.thehabit.attendance.command.app.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceInfoDTO {
    private int memberCode;
    private String memberName;
    private Double percentage;
    private List<LocalDateTime> dateList;

}
