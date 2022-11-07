package com.habit.thehabit.record.command.app.service;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    public List<RecordDTO> selectRecordListByISBN(String bookISBN) {
        System.out.println("bookISBN = " + bookISBN);

        /** 리스트 형태로 조회 */
        List<Record> recordList = recordInfraRepository.findByBookISBN(bookISBN);

        System.out.println("recordList = " + recordList);

        /** 리스트 안의 entity들을 DTO형태로 바꾼뒤, DTO 리스트로 전환 */
        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            System.out.println("recordDTOList = " + recordDTOList);
            recordDTOList.add(record.entityToDTO());
        }

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordDTOList == null){
            throw new RecordNotFoundException("ISBN에 해당하는 독서 기록이 없습니다.");
        }

        return recordDTOList;
    }

    public List<RecordDTO> selectRecordListByUserInfo(int memberCode) {
        System.out.println("memberCode = " + memberCode);

        List<Record> recordList = recordInfraRepository.findByMemberCode(memberCode);
        System.out.println("recordList = " + recordList);

        /** 리스트 안의 entity들을 DTO형태로 바꾼뒤, DTO 리스트로 전환 */
        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            System.out.println("recordDTOList = " + recordDTOList);
            recordDTOList.add(record.entityToDTO());
        }

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordDTOList == null){
            throw new RecordNotFoundException("유저의 독서 기록이 없습니다.");
        }

        return recordDTOList;
    }
}
