package com.habit.thehabit.record.command.infra.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.domain.repository.RecordRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordInfraRepository extends RecordRepository {

    Record findByRecordCodeAndIsActivated(Long recordCode, String isActivated);

    List<Record> findByBookISBNAndIsActivated(String bookISBN, String isActivated);

    List<Record> findByIsActivatedAndIsDoneOrderByRatingDesc(String isActivated, String isDone);

    List<Record> findByIsActivatedAndIsDoneOrderByRecordCode(String isActivated, String isDone);

    Record findByIsActivatedAndRecordCodeOrderByRecordCode(String isActivated, Long recordCode);

    @Query("select m from Record m where m.member.memberCode = :memberCode and m.isActivated = 'Y' and m.isOverHead = 'Y'")
    Record findByMemberCodeAndIsActivatedAndIsOverHead(int memberCode);

    @Query("select m from Record m where m.member.memberCode = :memberCode and m.isActivated = 'Y' ")
    List<Record> findByMemberCode(int memberCode);

    /*후기 등록한 목록중 평점 높은 순으로 반환*/
    @Query("select r from Record r where r.member.memberCode = :memberCode and r.isActivated = 'Y' and r.isDone = :isDone order by r.rating desc")
    List<Record> findByMemberCodeAndIsDone(int memberCode, String isDone);

    /*인생책 목록 중 평점 높은 순으로 반환*/
    @Query("select r from Record r where r.member.memberCode = :memberCode and r.isActivated = 'Y' and r.isDone = :isDone and r.isBest =:isBest order by r.rating desc")
    List<Record> findByMemberCodeAndIsDoneAndIsBest(int memberCode, String isDone, String isBest);

    @Query("select m from Record m where m.member.memberCode = :memberCode and m.isActivated = 'Y' and m.bookISBN = :bookISBN")
    List<Record> findByMemberCodeAndBookISBNAndIsActivated(int memberCode, String bookISBN);

    /**/
    List<Record> findAllByIsSent(String isSent);
}
