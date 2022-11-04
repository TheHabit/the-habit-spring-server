package com.habit.thehabit.member.command.app.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemberDTO {
    private  int memberCode;
    private String memberId;
    private String memberPwd;
    private String isTempPwd;
    private String name;
    private String phone;
    private Date signupDate;
    private Date withDrawalDate;
    private String isWithDrawal;
    private String memberRole;
}
