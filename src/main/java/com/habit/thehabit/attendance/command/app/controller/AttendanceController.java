package com.habit.thehabit.attendance.command.app.controller;

import com.habit.thehabit.attendance.command.app.dto.RegistAttendanceDTO;
import com.habit.thehabit.attendance.command.app.service.AttendanceService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/attendance")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO> regist(@RequestBody RegistAttendanceDTO registAttendanceDTO){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출석 성공", attendanceService.regist(registAttendanceDTO)));

    }

}
