package com.habit.thehabit.club.command.app.dto;

import com.habit.thehabit.club.command.domain.aggregate.ClubStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {
    private int clubId;
    private String clubName;
    private String bookName;
    private Date recruitStartDate; //모입시작일
    private Date recruitEndDate; //모집종료일
    private Date startDate; //활동시작일
    private Date endDate;   //활동종료일
    private int numberOfMember;
    private String status;
//    private List<Integer> memberCodeList;
}
