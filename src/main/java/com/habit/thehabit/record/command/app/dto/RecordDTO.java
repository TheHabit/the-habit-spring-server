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
    private String bookISBN;
    private Long bookGrade;

    private String pageSource;
    private String bookReview;
    private String oneLineReview;
    private String isActivated;

    private Date startDate;
    private Date endDate;
    private Date reportDate;

    private int memberCode;

    public ReadingPeriod setReadingPeriod(){
        ReadingPeriod readingPeriod = new ReadingPeriod(startDate, endDate, reportDate);

        return readingPeriod;
    }

    public Record dtoToEntity(Member member){
        Record record = new Record(recordCode, bookName, bookISBN, bookGrade,
                pageSource, bookReview, oneLineReview, isActivated, setReadingPeriod(), member);
        return record;
    }

}
