package com.habit.thehabit.common.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.common.command.app.exception.LoginFailedException;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthyCheckController {

    @GetMapping("")
    public ResponseEntity<ResponseDTO> healthyCheck() {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상 동작", "정상 동작"));
    }
}
