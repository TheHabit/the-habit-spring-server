package com.habit.thehabit.club.command.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubDTO {

    private int clubId;
    private String message;
    private String clubName;
    private String clubIntro;
    private String bookName;
    private LocalDateTime recruitStartDate; //모집시작일 -> 생성일자로 대체
    private LocalDateTime recruitEndDate; //모집종료일 --> 며칠간 모집할지 일수 받기
    private LocalDateTime startDate; //활동시작일 00:00
    private LocalDateTime endDate;   //활동종료일 23:59
    private int numberOfMember; //모집 인원
    private String status;
    private String imageUri; //이미지 정보
    private List<ScheduleDTO> scheduleDTOList; //미팅 일정들

}
