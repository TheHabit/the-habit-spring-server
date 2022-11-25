package com.habit.thehabit.member.command.app.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberAdminDTO {
    private int memberCode;
    private String memberId;
    private String nickName;
    private String memberRole;
    private String isWithDrawal;
}
