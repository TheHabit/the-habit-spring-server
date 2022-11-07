package com.habit.thehabit.record.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.app.service.RecordService;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/records")
public class RecordController {
    
    private final RecordService recordService;
    
    @Autowired
    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }

    /** 독서 기록 삽입 */
    @PostMapping("")
    public ResponseEntity<ResponseDTO> insertRecord(@RequestBody RecordDTO record, @AuthenticationPrincipal Member member){
        
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 입력 성공", recordService.insertRecord(record, member)));
        } catch(Exception e){
            System.out.println("Exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 입력 실패", "내부 에러 발생"));
        }
    }

    /** bookISBN으로 해당하는 독서록 리스트 찾아오기 */
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

    /** 회원 정보로 독서록 리스트 찾아오기 */
    @GetMapping("/user")
    public ResponseEntity<ResponseDTO> selectRecordListByUserInfo(@AuthenticationPrincipal Member member){
        int memberCode = member.getMemberCode();
        System.out.println("memberCode = " + memberCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 유저의 독서기록 조회", recordService.selectRecordListByUserInfo(memberCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

}
