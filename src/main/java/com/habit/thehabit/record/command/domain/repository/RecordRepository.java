package com.habit.thehabit.record.command.domain.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
