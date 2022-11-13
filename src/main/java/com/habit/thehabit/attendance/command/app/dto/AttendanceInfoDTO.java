package com.habit.thehabit.attendance.command.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceInfoDTO {
    private int memberCode;
    private String memberName;
    private Double percentage;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private List<LocalDateTime> dateList;

}
