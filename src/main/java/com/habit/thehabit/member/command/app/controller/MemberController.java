package com.habit.thehabit.member.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.app.dto.UpdateRequestDTO;
import com.habit.thehabit.member.command.app.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseBody
    @GetMapping("test")
    public String test(){
        return "MemberTest";
    }

    @ResponseBody
    @GetMapping("success")
    public String success(){
        return "You're successed to login!";
    }

    @ResponseBody
    @GetMapping("fail")
    public String fail(){
        return "You're failed to login!";
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO> updateMember(@RequestBody UpdateRequestDTO updateRequestDTO){
        System.out.println("컨트롤러에 요청들어왔습니다.");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "회원정보 수정 완료", memberService.updateMember(updateRequestDTO)));
    }
}
