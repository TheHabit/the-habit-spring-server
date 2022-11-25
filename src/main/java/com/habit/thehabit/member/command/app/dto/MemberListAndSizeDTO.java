package com.habit.thehabit.member.command.app.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberListAndSizeDTO {

    private List<MemberAdminDTO> memberAdminDTOList;
    private long totalPage;
}
