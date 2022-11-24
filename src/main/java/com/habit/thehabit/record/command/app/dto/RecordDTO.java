package com.habit.thehabit.record.command.app.dto;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.domain.aggregate.ReadingPeriod;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordDTO {
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
    private String isActivated;
    private String isBest;
    private Date startDate;
    private Date endDate;
    private Date reportDate;

    private int memberCode;

    private String isSent;

    public ReadingPeriod setReadingPeriod(){
        ReadingPeriod readingPeriod = new ReadingPeriod(startDate, endDate, reportDate);

        return readingPeriod;
    }

    public Record dtoToEntity(Member member){
        Record record = new Record(recordCode, bookName, bookAuthor, bookPublishInfo, thumbnailLink, bookISBN, rating,

        bookReview, oneLineReview, isDone, isActivated, isBest, setReadingPeriod(), isSent, member);

        return record;
    }

}
