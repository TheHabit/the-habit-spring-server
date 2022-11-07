package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_RECORD")
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

    @Column(name = "BOOK_ISBN")
    private String bookISBN;

    @Column(name = "PAGE_SOURCE")
    private String pageSource;

    @Column(name = "BOOK_REVIEW")
    private String bookReview;

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
        RecordDTO responseDTO = new RecordDTO(recordCode, bookName, bookISBN, bookReview,
                pageSource, readingPeriod.getStartDate(), readingPeriod.getEndDate(),
                readingPeriod.getReportDate(), member.getMemberCode());

        return responseDTO;
    }

    public Member getMember(){
        return member;
    }

    public Long getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(Long recordCode) {
        this.recordCode = recordCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getPageSource() {
        return pageSource;
    }

    public void setPageSource(String pageSource) {
        this.pageSource = pageSource;
    }

    public String getBookReview() {
        return bookReview;
    }

    public void setBookReview(String bookReview) {
        this.bookReview = bookReview;
    }

    public ReadingPeriod getReadingPeriod() {
        return readingPeriod;
    }

    public void setReadingPeriod(ReadingPeriod readingPeriod) {
        this.readingPeriod = readingPeriod;
    }

}
