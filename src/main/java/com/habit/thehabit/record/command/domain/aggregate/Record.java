package com.habit.thehabit.record.command.domain.aggregate;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.domain.aggregate.embeddable.RecordType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "TBL_RECORD")
@TableGenerator(
        name = "SEQ_RECORD_ID",
        table = "TBL_SEQ_RECORD",
        pkColumnValue = "RECORD_SEQ",
        allocationSize = 1
)
public abstract class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_RECORD_ID")
    @Column(name = "RECORD_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_CODE")
    private Member member; //회원정보

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECORD_DATE")
    private Date recordDate; //기록일

    @Enumerated(EnumType.STRING)
    private RecordType recordType; //enumtime CLUBREVIEW 혹은 PERSONALREVIEW



}
