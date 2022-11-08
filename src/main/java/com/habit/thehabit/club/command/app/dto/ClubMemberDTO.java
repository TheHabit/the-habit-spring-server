package com.habit.thehabit.club.command.app.dto;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.member.command.app.dto.MemberDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class ClubMemberDTO {
    /* club */
    private int clubId;
    private String clubName;
    private String bookName;
    private Date recruitStartDate; //모입시작일
    private Date recruitEndDate; //모집종료일
    private Date startDate; //활동시작일
    private Date endDate;   //활동종료일
    private int numberOfMember;
    /* Member */
    private List<Integer> memberCodeList;
}
