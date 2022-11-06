package com.habit.thehabit.record.command.app.service;

import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    private RecordInfraRepository recordInfraRepository;

    @Autowired
    public RecordService(RecordInfraRepository recordInfraRepository){
        this.recordInfraRepository = recordInfraRepository;
    }
    public Record insertRecord(Record record) {
        recordInfraRepository.save(record);

        return recordInfraRepository.findByRecordCode(record.getRecordCode());
    }
}
