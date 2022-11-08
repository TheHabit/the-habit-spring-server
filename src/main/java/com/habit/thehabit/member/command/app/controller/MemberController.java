package com.habit.thehabit.member.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.common.command.app.exception.DuplicateIdException;
import com.habit.thehabit.member.command.app.dto.UpdateRequestDTO;
import com.habit.thehabit.member.command.app.service.MemberService;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO> signup(@RequestBody Member member){

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", memberService.signup(member)));
        } catch (DuplicateIdException e){
            return ResponseEntity.status(409).body(new ResponseDTO(HttpStatus.CONFLICT, "회원가입 실패", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러", "서버 에러"));
        }
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO> updateMember(@RequestBody UpdateRequestDTO updateRequestDTO){
        try {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "회원정보 수정 완료", memberService.updateMember(updateRequestDTO)));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러", "서버 에러"));
        }
    }
}
