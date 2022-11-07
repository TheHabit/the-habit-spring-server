package com.habit.thehabit.record.command.infra.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.domain.repository.RecordRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordInfraRepository extends RecordRepository {

    Record findByRecordCode(Long recordCode);

    List<Record> findByBookISBN(String bookISBN);

    @Query("select m from Record m where m.member.memberCode = :memberCode")
    List<Record> findByMemberCode(int memberCode);
}
