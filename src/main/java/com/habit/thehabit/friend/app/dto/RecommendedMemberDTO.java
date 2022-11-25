package com.habit.thehabit.friend.app.dto;

import com.habit.thehabit.record.command.app.dto.RecordDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedMemberDTO {
    private  int memberCode;
    private String memberId;
    private String name;
    private List<RecordDTO> records;
}
