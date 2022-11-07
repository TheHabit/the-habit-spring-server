package com.habit.thehabit.record.command.infra.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.domain.repository.RecordRepository;

public interface RecordInfraRepository extends RecordRepository {

    Record findByRecordCode(Long recordCode);

    Record findByBookISBN(String bookISBN);
}
