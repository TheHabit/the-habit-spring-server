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
@RequestMapping("/v1/auths")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody Member member) {

        try {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "로그인 성공", authService.login(member)));
        } catch (LoginFailedException e){
            return ResponseEntity.status(401).body(new ResponseDTO(HttpStatus.UNAUTHORIZED, "로그인 실패", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러", "서버 에러"));
        }
    }
}

