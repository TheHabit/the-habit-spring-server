package com.habit.thehabit.club.command.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WithdrawDTO {
    private int clubId;
    private String clubName;
    private String memberName;
    private Date withdrawDate;
}

