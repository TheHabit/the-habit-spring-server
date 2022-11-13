package com.habit.thehabit.club.command.app.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private int clubId;
    private String message;
    private String clubName;
    private String clubIntro;
    private String bookName;
    private LocalDateTime recruitStartDate; //모입시작일
    private LocalDateTime recruitEndDate; //모집종료일
    private LocalDateTime startDate; //활동시작일
    private LocalDateTime endDate;   //활동종료일
    private int numberOfMember; //모집인원수
    private int currentNumberOfMemeber; //현재인원수
    private String status;
    private String imageUri; //이미지 정보
    private List<ScheduleDTO> scheduleDTOList;

}
