package com.habit.thehabit.record.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.service.RecordService;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/records")
public class RecordController {
    
    private final RecordService recordService;
    
    @Autowired
    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }
    
    @PostMapping("")
    public ResponseEntity<ResponseDTO> insertRecord(@RequestBody RecordDTO record, @AuthenticationPrincipal Member member){
        
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 입력 성공", recordService.insertRecord(record, member)));
        } catch(Exception e){
            System.out.println("Exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 입력 실패", "내부 에러 발생"));
        }
    }
    
}
