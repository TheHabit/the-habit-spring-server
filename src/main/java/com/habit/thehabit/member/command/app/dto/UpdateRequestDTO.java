package com.habit.thehabit.member.command.app.dto;

import lombok.Data;

@Data
public class UpdateRequestDTO {

    private int memberCode;
    private String memberId;
    private String memberPwd;
    private String isTempPwd;
    private String name;
    private String phone;
    private String signupDate;
    private String withDrawalDate;
    private String isWithDrawal;
    private String memberRole;

}
