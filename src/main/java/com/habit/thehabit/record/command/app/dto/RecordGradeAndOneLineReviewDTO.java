package com.habit.thehabit.record.command.app.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordGradeAndOneLineReviewDTO {
    private Long bookGrade;
    private String oneLineReview;
}
