package com.habit.thehabit.record.command.app.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordOneDTO {

    private Long recordCode;
    private String bookName;
    private String bookAuthor;
    private String bookPublishInfo;
    private String thumbnailLink;

    private String bookISBN;
    private int rating;

    private String bookReview;
    private String oneLineReview;
    private String isDone;
    private String isBest;

    private Date reportDate;

    private String name;
}
