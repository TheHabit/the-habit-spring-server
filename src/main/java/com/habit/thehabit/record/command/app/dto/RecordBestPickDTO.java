package com.habit.thehabit.record.command.app.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordBestPickDTO {

    List<RecordDTO> recordDTOList;

}

