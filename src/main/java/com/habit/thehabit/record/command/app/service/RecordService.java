package com.habit.thehabit.record.command.app.service;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RecordService {

    private RecordInfraRepository recordInfraRepository;

    @Autowired
    public RecordService(RecordInfraRepository recordInfraRepository){
        this.recordInfraRepository = recordInfraRepository;
    }


    @Transactional
    public RecordDTO insertRecord(RecordDTO recordDTO, Member member) {

        /** DB에 접근하기 위해 DTO를 엔티티로 변환 */
        System.out.println("recordDTO = " + recordDTO);
        Record record = recordDTO.dtoToEntity(member);
        System.out.println("record entity = " + record);

        /** DB에 record 저장 */
        recordInfraRepository.save(record);

        /** 영속성 컨텍스트에 잘 들어갔는지 조회 */
        Record foundRecord = recordInfraRepository.findByRecordCode(record.getRecordCode());
        System.out.println("foundRecord = " + foundRecord);

        /** 응답하기 위해 다시 entity를 DTO로 반환 */
        RecordDTO responseDTO = foundRecord.entityToDTO();

        return responseDTO;
    }
}
