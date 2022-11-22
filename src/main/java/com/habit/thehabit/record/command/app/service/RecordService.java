package com.habit.thehabit.record.command.app.service;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.dto.RecordGradeAndOneLineReviewDTO;
import com.habit.thehabit.record.command.app.exception.DuplicateRecordException;
import com.habit.thehabit.record.command.app.exception.RecordDeleteException;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.domain.aggregate.ReadingPeriod;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import com.habit.thehabit.util.AwsFileUploadUtils;
import com.habit.thehabit.util.ReviewToOneLineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    private RecordInfraRepository recordInfraRepository;
    private AwsFileUploadUtils awsFileUploadUtils;
    private ReviewToOneLineUtils reviewToOneLineUtils;

    @Autowired
    public RecordService(RecordInfraRepository recordInfraRepository, AwsFileUploadUtils awsFileUploadUtils, ReviewToOneLineUtils reviewToOneLineUtils){
        this.recordInfraRepository = recordInfraRepository;
        this.awsFileUploadUtils = awsFileUploadUtils;
        this.reviewToOneLineUtils = reviewToOneLineUtils;
    }

    @Transactional
    public RecordDTO addRecord(MultipartFile bookImg, RecordDTO recordDTO, Member member) throws IOException {

        /** 이미 서재에 담았거나, 작성한 독서라면 예외처리 */
        List<Record> recordList = recordInfraRepository.findByMemberCodeAndBookISBNAndIsActivated(member.getMemberCode(), recordDTO.getBookISBN());

        if(recordList.size() >= 1){
            throw new DuplicateRecordException("이미 등록된 도서가 존재합니다.");
        }

        /** DB에 접근하기 위해 DTO를 엔티티로 변환 */
        System.out.println("recordDTO = " + recordDTO);
        Record record = recordDTO.dtoToEntity(member);
        System.out.println("record entity = " + record);
        
        /** 시작 날짜 (현재 날짜) 기록 */
        Date curDate = new Date();
        System.out.println("curDate = " + curDate);

        ReadingPeriod readingPeriod = new ReadingPeriod(curDate, null, null);

        System.out.println("record.getReadingPeriod() = " + record.getReadingPeriod());
        record.setReadingPeriod(readingPeriod);

        /** 읽을 책 담기임으로, isDone 'N'로 설정 */
        record.setIsDone("N");

        /** Aws S3로 파일 업로드 */
        String bookImgSrc = awsFileUploadUtils.fileUpload(bookImg, "record/thumbnail");
        record.setThumbnailLink(bookImgSrc);


        /** DB에 record 저장 */
        recordInfraRepository.save(record);

        /** 영속성 컨텍스트에 잘 들어갔는지 조회 */
        Record foundRecord = recordInfraRepository.findByRecordCodeAndIsActivated(record.getRecordCode(), "Y");
        System.out.println("foundRecord = " + foundRecord);

        /** 응답하기 위해 다시 entity를 DTO로 반환 */
        RecordDTO responseDTO = foundRecord.entityToDTO();

        return responseDTO;
    }

    @Transactional
    public RecordDTO writeRecord(MultipartFile bookImg, RecordDTO recordDTO, Member member) throws Exception {

        /** 영속성 컨텍스트에서 해당 레코드 조회 */
        List<Record> recordList = recordInfraRepository.findByMemberCodeAndBookISBNAndIsActivated(member.getMemberCode(), recordDTO.getBookISBN());

        Record record;

        if(recordList.size() == 0){
            /** 책을 먼저 담아야 작성할 수 있다. */
            throw new Exception("책을 먼저 담아주세요.");
        }

        if(recordList.size() > 1){
            /** 해당 회원이 같은 책을 다르게 2번 씀. 내부 로직상 있을 수 없는 오류 */
            throw new Exception();
        }

        if(recordList.size() == 0){
            /** 독서 기록 작성 */

            /** isDone 값 N으로 설정 */
            recordDTO.setIsDone("N");

            /** DB에 접근하기 위해 DTO를 엔티티로 변환 */
            record = recordDTO.dtoToEntity(member);
            System.out.println("record entity = " + record);

            /** report 날짜 (현재 날짜)만 기록 */
            Date curDate = new Date();
            System.out.println("curDate = " + curDate);

            ReadingPeriod readingPeriod = new ReadingPeriod(null, null, curDate);

            System.out.println("record.getReadingPeriod() = " + record.getReadingPeriod());
            record.setReadingPeriod(readingPeriod);

            /** Aws S3로 책 표지 이미지 파일 업로드 */
            String bookImgSrc = awsFileUploadUtils.fileUpload(bookImg, "record/thumbnail");
            record.setThumbnailLink(bookImgSrc);

            /** AI로부터 한 줄 요약을 가져오는 utill */
//            String oneLineReview = reviewToOneLineUtils.abStractOneLine(record.getBookReview());
//            record.setOneLineReview(oneLineReview);

            /** DB에 record 저장 */
            recordInfraRepository.save(record);

        } else {
            /** 독서 기록 수정 */
            record = recordList.get(0);

            /** review, rating 수정 */
            if(recordDTO.getRating() != record.getRating()){
                record.setRating(recordDTO.getRating());
            }

            /** AI로부터 한 줄 요약을 가져오는 utill */
            /** review가 수정되었을 경우에만 AI를 호출하여 oneLineReview 수정 */
            if(!recordDTO.getBookReview().equals(record.getBookReview())){
                System.out.println("test");
                record.setBookReview(recordDTO.getBookReview());

                String oneLineReview = reviewToOneLineUtils.abStractOneLine(recordDTO.getBookReview());
                record.setOneLineReview(oneLineReview);
            }

            /** 기록 날짜만 변경 */
            Date curDate = new Date();
            System.out.println("curDate = " + curDate);

            if(record.getReadingPeriod() == null){
                throw new Exception();
            }

            record.getReadingPeriod().setReportDate(curDate);

        }

        /** 응답하기 위해 다시 entity를 DTO로 반환 */
        RecordDTO responseDTO = record.entityToDTO();

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

    public List<RecordDTO> selectRecordByUserInfoIsDone(int memberCode) {

        List<Record> recordList = recordInfraRepository.findByMemberCodeAndIsDone(memberCode, "Y");
        System.out.println("recordList = " + recordList);

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordList == null){
            throw new RecordNotFoundException("독서 기록이 존재하지 않습니다.");
        }

        /** Entity List -> DTO List */
        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            recordDTOList.add(record.entityToDTO());
        }

        return recordDTOList;
    }

    public List<RecordDTO> selectRecordByUserInfo(int memberCode) {

        List<Record> recordList = recordInfraRepository.findByMemberCode(memberCode);
        System.out.println("recordList = " + recordList);

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordList == null){
            throw new RecordNotFoundException("유저의 독서 기록이 존재하지 않습니다.");
        }

        /** 리스트 안의 entity들을 DTO형태로 바꾼 뒤, DTO 리스트로 전환 */
        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            recordDTOList.add(record.entityToDTO());
        }

        return recordDTOList;
    }

    public List<RecordGradeAndOneLineReviewDTO> selectAllRecordGradeAndOneLineReview() {

        List<Record> recordList = recordInfraRepository.findByIsActivatedAndIsDoneOrderByRatingDesc("Y", "Y");
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
        if((Integer)recordDTO.getRating() != null){
            record.setRating(recordDTO.getRating());
        }
        if(recordDTO.getBookReview() != null){
            record.setBookReview(recordDTO.getBookReview());
        }
        /** 한줄평 들어갈 코드 추가 작성 필요 */
        if(recordDTO.getBookISBN() != null){
            record.setBookISBN(recordDTO.getBookISBN());
        }
        if(recordDTO.getBookName() != null){
            record.setBookName(recordDTO.getBookName());
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
