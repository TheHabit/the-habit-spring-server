package com.habit.thehabit.record.command.app.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordGradeAndOneLineReviewDTO {
    private String bookName;
    private String thumbnailLink;
    private String name;
    private String oneLineReview;
}
