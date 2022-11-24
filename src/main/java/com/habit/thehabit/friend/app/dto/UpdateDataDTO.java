package com.habit.thehabit.friend.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDataDTO {
    private int memberCode;
    private int rating;
    private String bookISBN;
}
