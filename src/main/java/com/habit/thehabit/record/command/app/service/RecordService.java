package com.habit.thehabit.record.command.app.service;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RefreshScope
public class RecordService {

    private static final RestTemplate REST_TEMPLATE;
    @Value("${ai.datasource.url}")
    private String url;

    static {
        // RestTemplate 기본 설정을 위한 Factory 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(10000);
        factory.setBufferRequestBody(false);
        REST_TEMPLATE = new RestTemplate(factory);
    }

    private RecordInfraRepository recordInfraRepository;
    private AwsFileUploadUtils awsFileUploadUtils;

    @Autowired
    public RecordService(RecordInfraRepository recordInfraRepository, AwsFileUploadUtils awsFileUploadUtils){
        this.recordInfraRepository = recordInfraRepository;
        this.awsFileUploadUtils = awsFileUploadUtils;
    }

    @Transactional
    public RecordDTO insertRecord(MultipartFile bookImg, RecordDTO recordDTO, Member member) throws IOException {

        /** 이미 서재에 담았거나, 작성한 독서라면 예외처리 */
        List<Record> recordList = recordInfraRepository.findByMemberCodeAndBookISBNAndIsActivated(member.getMemberCode(), recordDTO.getBookISBN());

        if(recordList.size() >= 1){
            throw new DuplicateRecordException("이미 등록된 도서가 존재합니다.");
        }

        /** DB에 접근하기 위해 DTO를 엔티티로 변환 */
        System.out.println("recordDTO = " + recordDTO);
        Record record = recordDTO.dtoToEntity(member);
        System.out.println("record entity = " + record);

        System.out.println("bookImg = " + bookImg);

        System.out.println("record.getBookReview() = " + record.getBookReview());
        
        /** 시작 날짜 (현재 날짜) 기록 */
        Date curDate = new Date();
        System.out.println("curDate = " + curDate);

        ReadingPeriod readingPeriod = new ReadingPeriod(curDate, null, null);

        System.out.println("record.getReadingPeriod() = " + record.getReadingPeriod());
        record.setReadingPeriod(readingPeriod);

        /** 읽을 책 담기임으로, isDone 'N'로 설정 */
        record.setIsDone("N");

        /** Aws S3로 파일 업로드 */
        String imgSrc = awsFileUploadUtils.fileUpload(bookImg, "record");
        record.setThumbnailLink(imgSrc);

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

        /** 영속성 컨텍스트에서 해당 레코드 조회(추후 로직 수정 필요) */
        List<Record> recordList = recordInfraRepository.findByMemberCodeAndBookISBNAndIsActivated(member.getMemberCode(), recordDTO.getBookISBN());

        Record record;
        if(recordList.size() == 0){
            /** 처음부터 쓰는 독서기록 */
            /** DB에 접근하기 위해 DTO를 엔티티로 변환 */
            record = recordDTO.dtoToEntity(member);
            System.out.println("record entity = " + record);

            /** report 날짜 (현재 날짜)만 기록 : 시작, 종료 날짜는 알 수 없다 -> 로직 수정 필요 */
            Date curDate = new Date();
            System.out.println("curDate = " + curDate);

            ReadingPeriod readingPeriod = new ReadingPeriod(null, null, curDate);

            System.out.println("record.getReadingPeriod() = " + record.getReadingPeriod());
            record.setReadingPeriod(readingPeriod);

            /** 독서기록임으로, isDone 'Y'로 설정 */
            record.setIsDone("Y");

//        /** Aws S3로 파일 업로드 */
//        String imgSrc = awsFileUploadUtils.fileUpload(bookImg, "record");
//        record.setPageSource(imgSrc);

//        /** ------------- 인공지능 API 서버로 review 전송하여 oneLineReview 얻기 */
//        /** body 설정 */
//        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("review", record.getBookReview());
//
//        /** header 설정 */
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        /** http 통신할 entity 설정 */
//        HttpEntity<?> requestEntity = new HttpEntity<>(body,headers);
//
//        /** AI 서버 통신 후 한줄 요약(oneLineReview) 가져오기 */
//        JsonNode response = REST_TEMPLATE.postForObject(url, requestEntity, JsonNode.class);
//        System.out.println("response = " + response);
//
//        String oneLineReview = null;
//
//        if(response != null){
//            oneLineReview = String.valueOf(response.get("result"));
//        }
//
//        /** record entity에 oneLineReview set */
//        record.setOneLineReview(oneLineReview);
//
//        /** ------------- */

            /** DB에 record 저장 */
            recordInfraRepository.save(record);

        } else if(recordList.size() == 1){
            /** 담아 놓은 책 수정 */
            record = recordList.get(0);

            /** isDone Y로 바꾸기 + review 담기 */
            record.setIsDone("Y");
            record.setBookReview(recordDTO.getBookReview());

//        /** ------------- 인공지능 API 서버로 review 전송하여 oneLineReview 얻기 */
//        /** body 설정 */
//        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("review", record.getBookReview());
//
//        /** header 설정 */
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        /** http 통신할 entity 설정 */
//        HttpEntity<?> requestEntity = new HttpEntity<>(body,headers);
//
//        /** AI 서버 통신 후 한줄 요약(oneLineReview) 가져오기 */
//        JsonNode response = REST_TEMPLATE.postForObject(url, requestEntity, JsonNode.class);
//        System.out.println("response = " + response);
//
//        String oneLineReview = null;
//
//        if(response != null){
//            oneLineReview = String.valueOf(response.get("result"));
//        }
//
//        /** record entity에 oneLineReview set */
//        record.setOneLineReview(oneLineReview);
//
//        /** ------------- */

            /** 기록 및 종료 날짜(현재 날짜) 기록 (추후 기록 날짜와 종료 날짜 분리 여부 논의 필요) */
            Date curDate = new Date();
            System.out.println("curDate = " + curDate);

            if(record.getReadingPeriod() == null){
                throw new Exception();
            }

            record.getReadingPeriod().setReportDate(curDate);
            record.getReadingPeriod().setEndDate(curDate);


        /** Aws S3로 파일 업로드 */
        String imgSrc = awsFileUploadUtils.fileUpload(bookImg, "record");
        record.setThumbnailLink(imgSrc);
        } else{
            throw new DuplicateRecordException("동일한 책의 독서기록이 존재합니다.");
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

    /* 2022-11-13 수정, 권수뿐만 아니라 다른 데이터들 같이 반환 */
    public List<RecordDTO> countingRecordByUserInfo(int memberCode) {
        System.out.println("memberCode = " + memberCode);

        List<Record> recordList = recordInfraRepository.findByMemberCodeAndIsDone(memberCode, "Y");
        System.out.println("recordList = " + recordList);

        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            recordDTOList.add(record.entityToDTO());
        }
        return recordDTOList;

//        /** 조회된 것이 없을 때 0 반환 */
//        if(recordList == null){
//            return 0;
//        }
//
//        return recordList.size();
    }

    public List<RecordDTO> selectRecordListByUserInfo(int memberCode) {
        System.out.println("memberCode = " + memberCode);

        List<Record> recordList = recordInfraRepository.findByMemberCodeAndIsDone(memberCode, "N");
        System.out.println("recordList = " + recordList);

        /** 리스트 안의 entity들을 DTO형태로 바꾼 뒤, DTO 리스트로 전환 */
        List<RecordDTO> recordDTOList = new ArrayList<>();
        for(Record record : recordList){
            recordDTOList.add(record.entityToDTO());
        }

        /** 조회된 것이 없을 때 예외 처리 */
        if(recordDTOList == null){
            throw new RecordNotFoundException("유저가 읽고 있는 책이 없습니다.");
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
