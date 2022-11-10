package com.habit.thehabit.club.command.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
public class CreateClubDTO {

    private int clubId;
    private String clubName;
    private String bookName;
    private Date recruitStartDate; //모입시작일
    private Date recruitEndDate; //모집종료일
    private Date startDate; //활동시작일
    private Date endDate;   //활동종료일
    private int numberOfMember;
    private String status;
    private String imageUri; //이미지 정보
    private List<ScheduleDTO> scheduleDTOList; //미팅 일정들

}
