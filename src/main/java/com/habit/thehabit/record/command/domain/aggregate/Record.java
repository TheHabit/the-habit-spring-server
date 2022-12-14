package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.friend.app.dto.UpdateDataDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordAdminDTO;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.dto.RecordGradeAndOneLineReviewDTO;
import com.habit.thehabit.record.command.app.dto.RecordOneDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_RECORD")
@Setter
@Getter
@TableGenerator(
        name = "RECORD_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "RECORD_SEQ",
        allocationSize = 1
)
@ToString
public class Record {

    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "RECORD_SEQ_GENERATOR"
    )
    @Column(name = "RECORD_CODE")
    private Long recordCode;

    @Column(name = "BOOK_NAME")
    private String bookName;

    @Column(name = "BOOK_AUTHOR")
    private String bookAuthor;

    @Column(name = "BOOK_PUBLISH_INFO")
    private String bookPubllishInfo;

    @Column(name = "THUMBNAIL_LINK")
    private String thumbnailLink;

    @Column(name = "BOOK_ISBN")
    private String bookISBN;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "BOOK_REVIEW")
    private String bookReview;

    @Column(name = "ONE_LINE_REVIEW")
    private String oneLineReview;

    @Column(name = "IS_DONE")
    private String isDone;

    @Column(name = "IS_ACTIVATED")
    private String isActivated;

    @Column(name = "IS_BEST")
    private String isBest;

    @Column(name = "IS_OVER_HEAD")
    private String isOverHead;

    @PrePersist
    public void prePersist(){

        this.isActivated = this.isActivated == null ? "Y" : this.isActivated;
        this.isBest = this.isBest == null ? "N" : this.isBest;
        this.isOverHead = this.isOverHead == null ? "N" : this.isOverHead;

    }

    @Embedded
    private ReadingPeriod readingPeriod;

    @Column(name="IS_SENT")
    private String isSent;
    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;


    /** ?????? ???????????? ?????? ??????????????? ???????????? ?????? ????????? */
    public void setMember(Member member){
        this.member = member;

        if(!member.getRecords().contains(this)){
            member.getRecords().add(this);
        }
    }

    public RecordDTO entityToDTO(){
        RecordDTO responseDTO;

        if(readingPeriod != null){
            responseDTO = new RecordDTO(recordCode, bookName, bookAuthor, bookPubllishInfo, thumbnailLink, bookISBN, rating,
                    bookReview, oneLineReview, isDone, isActivated, isBest, isOverHead, readingPeriod.getStartDate(), readingPeriod.getEndDate(),
                    readingPeriod.getReportDate(), member.getMemberCode(), isSent);

        } else{
            responseDTO = new RecordDTO(recordCode, bookName, bookAuthor, bookPubllishInfo, thumbnailLink, bookISBN, rating,
                    bookReview, oneLineReview, isDone, isActivated, isBest, isOverHead, null, null, null, member.getMemberCode(), isSent);
        }

        return responseDTO;
    }

    public RecordGradeAndOneLineReviewDTO entitiyToOneLineReviewDTO(){
        RecordGradeAndOneLineReviewDTO responseDTO;

        responseDTO = new RecordGradeAndOneLineReviewDTO(bookName, thumbnailLink, member.getName(), oneLineReview);

        return responseDTO;
    }

    public RecordAdminDTO entityToAdminDTO(){
        RecordAdminDTO responseDTO;

        responseDTO = new RecordAdminDTO(recordCode, bookName, bookISBN, bookAuthor, member.getName(), rating);

        return responseDTO;
    }
    
    public UpdateDataDTO entityToUpdateDataDTO(){
        UpdateDataDTO updateDataDTO = new UpdateDataDTO(this.member.getMemberCode(),this.rating, this.bookISBN);

        return updateDataDTO;
    }

    public RecordOneDTO entityToRecordOneDTO(){
        RecordOneDTO recordOneDTO;

        if(readingPeriod != null){
            recordOneDTO = new RecordOneDTO(recordCode, bookName, bookAuthor, bookPubllishInfo, thumbnailLink, bookISBN, rating,
                    bookReview, oneLineReview, isDone, isBest, readingPeriod.getReportDate(), member.getName());
        } else{
            recordOneDTO = new RecordOneDTO(recordCode, bookName, bookAuthor, bookPubllishInfo, thumbnailLink, bookISBN, rating,
                    bookReview, oneLineReview, isDone, isBest, null, member.getName());
        }

        return recordOneDTO;
    }

}
