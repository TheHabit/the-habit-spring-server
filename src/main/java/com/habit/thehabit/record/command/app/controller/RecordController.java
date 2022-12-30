package com.habit.thehabit.record.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordBestPickDTO;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.exception.DuplicateRecordException;
import com.habit.thehabit.record.command.app.exception.RecordDeleteException;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.app.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/records")
public class RecordController {
    
    private final RecordService recordService;
    
    @Autowired
    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }


    /** 도서 담기 */
    @PostMapping(value = "/contain" /** consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE } */)
    public ResponseEntity<ResponseDTO> addRecord(@RequestPart(value = "bookImg") @Nullable MultipartFile bookImg,
                                                 RecordDTO record , @AuthenticationPrincipal Member member){
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "도서 담기 성공", recordService.addRecord(bookImg, record, member)));
        } catch (DuplicateRecordException e){
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseDTO(HttpStatus.CONFLICT, "이미 담긴 도서입니다.", "이미 담긴 도서입니다."));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "도서 담기 실패", "내부 에러 발생"));
        }
    }


    /** 독서 기록 작성 */
    @PostMapping(value = "/write")
    public ResponseEntity<ResponseDTO> writeRecord( @RequestBody RecordDTO record , @AuthenticationPrincipal Member member){
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 쓰기 성공", recordService.writeRecord(record, member)));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 쓰기 실패", e.getMessage()));
        }
    }

    /** 인생 도서 등록 */
    @PostMapping("/best")
    public ResponseEntity<ResponseDTO> setAllTimeBook(@RequestBody RecordBestPickDTO recordBestPickDTO, @AuthenticationPrincipal Member member){

        System.out.println("recordBestPickDTO = " + recordBestPickDTO);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "인생 도서 등록 성공", recordService.setAllTimeBook(recordBestPickDTO.getRecordList(), member)));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "인생 도서 등록 실패", e.getMessage()));
        }
    }


    /** bookISBN으로 독서기록 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> selectRecordListByISBN(@RequestParam String bookISBN){
        System.out.println("bookISBN = " + bookISBN);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 ISBN의 독서기록 조회", recordService.selectRecordListByISBN(bookISBN)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** RecordCode로 독서기록 조회 */
    @GetMapping("one")
    public ResponseEntity<ResponseDTO> selectRecordByRecordCode(@RequestParam String recordCode){
        System.out.println("recordCode = " + recordCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 Record Code 독서기록 조회", recordService.selectRecordByRecordCode(recordCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "해당 Record Code 독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** 회원 정보로 모든 독서기록 조회 */
    @GetMapping("/desk")
    public ResponseEntity<ResponseDTO> selectRecordByUserInfo(@AuthenticationPrincipal Member member){
        int memberCode = member.getMemberCode();
        System.out.println("memberCode = " + memberCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "유저의 모든 도서 기록 반환", recordService.selectRecordByUserInfo(memberCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "도서 기록이 없습니다.", re.getMessage()));
        } catch (Exception e){
            System.out.println("Exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "유저가 읽고 있는 도서 조회 실패", "내부 에러 발생"));
        }
    }

    /** 회원 정보로 완독한 독서기록 조회 */
    @GetMapping("/myroom")
    public ResponseEntity<ResponseDTO> selectRecordByUserInfoIsDone(@AuthenticationPrincipal Member member){
        int memberCode = member.getMemberCode();
        System.out.println("memberCode = " + memberCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "유저의 모든 독서 기록 조회 성공", recordService.selectRecordByUserInfoIsDone(memberCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "도서 기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "유저가 다 읽은 도서 권수 조회 실패", "내부 에러 발생"));
        }
    }

    /** 전체 독서기록에서 평점, 한줄평 조회 */
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> selectAllRecordOneLineReview(){

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "전체 독서기록 한줄평 가져오기", recordService.selectAllRecordGradeAndOneLineReview()));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** 관리자 - 전체 독서기록 조회 */
    @GetMapping("/all-admin")
    public ResponseEntity<ResponseDTO> selectAllRecord(){

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "전체 독서 기록 리스트 가져오기", recordService.selectAllRecord()));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** 독서기록 수정 */
    @PatchMapping("")
    public ResponseEntity<ResponseDTO> updateRecord(@RequestBody RecordDTO recordDTO, @AuthenticationPrincipal Member member){

        System.out.println("recordDTO = " + recordDTO);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 수정 성공", recordService.updateRecord(recordDTO, member)));
        } catch (Exception e){
            System.out.println("Exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 수정 실패", "내부 에러"));
        }
    }

    /** 독서기록 삭제 */
    @DeleteMapping("")
    public ResponseEntity<ResponseDTO> deleteRecord(@RequestParam Long recordCode){
        System.out.println("recordCode = " + recordCode);
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 삭제 성공", "삭제 완료되었습니다."));
        } catch (RecordDeleteException rde){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 삭제 실패", rde.getMessage()));
        } catch(Exception e){
            System.out.println("exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 삭제 실패", "내부 에러"));
        }
    }
}
