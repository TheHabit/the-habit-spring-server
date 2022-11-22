package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.dto.RecordGradeAndOneLineReviewDTO;
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

    @PrePersist
    public void prePersist(){
        this.isActivated = this.isActivated == null ? "Y" : this.isActivated;
    }

    @Embedded
    private ReadingPeriod readingPeriod;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    /** 해당 레코드에 멤버 연관관계를 추가하는 편의 메소드 */
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
                    bookReview, oneLineReview, isDone, isActivated, isBest, readingPeriod.getStartDate(), readingPeriod.getEndDate(),
                    readingPeriod.getReportDate(), member.getMemberCode());

        } else{
            responseDTO = new RecordDTO(recordCode, bookName, bookAuthor, bookPubllishInfo, thumbnailLink, bookISBN, rating,
                    bookReview, oneLineReview, isDone, isActivated, isBest, null, null, null, member.getMemberCode());
        }

        return responseDTO;
    }

    public RecordGradeAndOneLineReviewDTO entitiyToOneLineReviewDTO(){
        RecordGradeAndOneLineReviewDTO responseDTO;

        responseDTO = new RecordGradeAndOneLineReviewDTO(bookName, thumbnailLink, member.getName(), oneLineReview);

        return responseDTO;
    }

}
