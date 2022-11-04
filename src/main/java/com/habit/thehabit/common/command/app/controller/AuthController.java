package com.habit.thehabit.common.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.common.command.app.service.AuthService;
import com.habit.thehabit.common.command.app.exception.LoginFailedException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auths")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("test")
    public String test(){
        return "hello guys";
    }

    @PostMapping("signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody Member member){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", authService.signup(member)));
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody Member member) throws LoginFailedException {
        System.out.println("member = " + member);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "로그인 성공", authService.login(member)));
    }

//    @PutMapping("update")
//    public ResponseEntity<ResponseDTO> update(@RequestBody Member member, @AuthenticationPrincipal User user){
//        System.out.println("member = " + member);
//        System.out.println("user = " + user);
//
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "회원정보 수정 성공", authService.update(member, user)));
//    }

}

