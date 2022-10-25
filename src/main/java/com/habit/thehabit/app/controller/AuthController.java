package com.habit.thehabit.app.controller;

import com.habit.thehabit.app.dto.ResponseDTO;
import com.habit.thehabit.app.dao.entity.Member;
import com.habit.thehabit.app.service.AuthService;
import com.habit.thehabit.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "Success to sign-up", authService.signup(member)));
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody Member member) throws LoginFailedException {
        System.out.println("member = " + member);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "로그인 성공", authService.login(member)));
    }
}

