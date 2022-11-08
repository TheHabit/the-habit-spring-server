package com.habit.thehabit.record.command.app.service;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.dto.RecordGradeAndOneLineReviewDTO;
import com.habit.thehabit.record.command.app.exception.RecordDeleteException;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.domain.aggregate.ReadingPeriod;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Record foundRecord = recordInfraRepository.findByRecordCodeAndIsActivated(record.getRecordCode(), "Y");
        System.out.println("foundRecord = " + foundRecord);

        /** 응답하기 위해 다시 entity를 DTO로 반환 */
        RecordDTO responseDTO = foundRecord.entityToDTO();

        return responseDTO;
    }

    public List<RecordDTO> selectRecordListByISBN(String bookISBN) {
        System.out.println("bookISBN = " + bookISBN);

        /** 리스트 형태로 조회 */
        List<Record> recordList = recordInfraRepository.findByBookISBNAndIsActivated(bookISBN, "Y");

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
            recordDTOList.add(record.entityToDTO());
        }

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordDTOList == null){
            throw new RecordNotFoundException("유저의 독서 기록이 없습니다.");
        }

        return recordDTOList;
    }

    public List<RecordGradeAndOneLineReviewDTO> selectAllRecordGradeAndOneLineReview() {

        List<Record> recordList = recordInfraRepository.findByIsActivatedOrderByBookGradeDesc("Y");
        System.out.println("recordList = " + recordList);

        /** 평점과 한줄평만 가져오는 DTO를 만들어서 진행 */
        List<RecordGradeAndOneLineReviewDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            recordDTOList.add(record.entitiyToOneLineReviewDTO());
        }

        if(recordDTOList == null){
            throw new RecordNotFoundException("전체 독서 기록이 없습니다.");
        }

        return recordDTOList;
    }

    @Transactional
    public RecordDTO updateRecord(RecordDTO recordDTO, Member member) {

        /** 영속성 컨텍스트에 접근해 해당 record 엔티티 가져오기 */
        Record record = recordInfraRepository.findByRecordCodeAndIsActivated(recordDTO.getRecordCode(), "Y");
        System.out.println("record = " + record);

        /** 받아온 값 중, null 값이 아닌, 즉 수정하고자 하는 값들만 setter 이용하여 세팅. */
        if(recordDTO.getBookGrade() != null){
            record.setBookGrade(recordDTO.getBookGrade());
        }
        if(recordDTO.getBookReview() != null){
            record.setBookReview(recordDTO.getBookReview());
        }
        /** 한줄평 들어갈 코드 추가 작성 필요 */
        if(recordDTO.getBookISBN() != null){
            record.setBookISBN(recordDTO.getBookISBN());
        }
        if(recordDTO.getBookGrade() != null){
            record.setBookName(recordDTO.getBookName());
        }
        if(recordDTO.getPageSource() != null){
            record.setPageSource(recordDTO.getPageSource());
        }

        ReadingPeriod readingPeriod = new ReadingPeriod(recordDTO.getStartDate(), recordDTO.getEndDate(), recordDTO.getReportDate());

        /** null pointer exception 처리를 위한 if 문 */
        if(record.getReadingPeriod() != null) {

            /** null값일 경우, 수정하지 않고 기존 것을 쓰는 것으로 판단 */
            if(recordDTO.getStartDate() == null) {
                readingPeriod.setStartDate(record.getReadingPeriod().getStartDate());
            }

            if(recordDTO.getEndDate() == null) {
                readingPeriod.setEndDate(record.getReadingPeriod().getEndDate());
            }

            if(recordDTO.getReportDate() == null) {
                readingPeriod.setReportDate(record.getReadingPeriod().getReportDate());
            }
        }

        record.setReadingPeriod(readingPeriod);

        RecordDTO responseDTO = record.entityToDTO();
        System.out.println("responseDTO = " + responseDTO);

        return responseDTO;
    }

    @Transactional
    public boolean deleteRecord(Long recordCode) {

        /** 영속성 컨텍스트에서 가져와, isActivated 컬럼을 N으로 바꿔서 soft delete 수행 */
        Record record = recordInfraRepository.findByRecordCodeAndIsActivated(recordCode, "Y");
        System.out.println("record = " + record);

        if(record == null){
           throw new RecordDeleteException("존재하지 않는 독서기록입니다.");
        }

        record.setIsActivated("N");
        if(record.getIsActivated() != "N"){
            throw new RuntimeException();
        }
        return true;
    }
}
