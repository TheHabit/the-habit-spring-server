package com.habit.thehabit.club.command.app.dto;

import lombok.*;

import java.util.Date;

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
    private Date recruitStartDate; //모입시작일
    private Date recruitEndDate; //모집종료일
    private Date startDate; //활동시작일
    private Date endDate;   //활동종료일
    private int numberOfMember;
    private String status;
    private String imageUri; //이미지 정보

//    private List<ScheduleDTO> scheduleDTOList;


}
