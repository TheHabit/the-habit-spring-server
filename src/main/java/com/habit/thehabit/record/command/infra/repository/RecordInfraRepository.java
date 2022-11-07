package com.habit.thehabit.record.command.infra.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.domain.repository.RecordRepository;

import java.util.List;

public interface RecordInfraRepository extends RecordRepository {

    Record findByRecordCode(Long recordCode);

    List<Record> findByBookISBN(String bookISBN);
}
