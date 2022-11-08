package com.habit.thehabit.record.command.domain.repository;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {


}
