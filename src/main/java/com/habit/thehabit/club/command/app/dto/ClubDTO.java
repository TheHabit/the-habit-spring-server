package com.habit.thehabit.club.command.app.dto;

import com.habit.thehabit.club.command.domain.aggregate.ClubStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ClubDTO {
    private String clubName;
    private String bookName;
    private Date recruitStartDate; //모입시작일
    private Date recruitEndDate; //모집종료일
    private Date startDate; //활동시작일
    private Date endDate;   //활동종료일
    private int numberOfMember;
}
