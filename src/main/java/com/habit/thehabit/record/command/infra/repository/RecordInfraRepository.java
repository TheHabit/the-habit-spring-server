package com.habit.thehabit.record.command.infra.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.domain.repository.RecordRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordInfraRepository extends RecordRepository {

    Record findByRecordCodeAndIsActivated(Long recordCode, String isActivated);

    List<Record> findByBookISBNAndIsActivated(String bookISBN, String isActivated);

    List<Record> findByIsActivated(String isActivated);

    @Query("select m from Record m where m.member.memberCode = :memberCode and m.isActivated = 'Y' ")
    List<Record> findByMemberCode(int memberCode);
}
