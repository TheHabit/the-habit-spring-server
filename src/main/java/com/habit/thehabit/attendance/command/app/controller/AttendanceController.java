package com.habit.thehabit.attendance.command.app.controller;

import com.habit.thehabit.attendance.command.app.dto.RegistAttendanceDTO;
import com.habit.thehabit.attendance.command.app.service.AttendanceService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/v1/attendances")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /* 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> findAllAttendance(@RequestParam(value = "clubId", defaultValue = "")int clubId){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", attendanceService.findAllAttendance(clubId)));
    }

    /* 출석 */
    @PostMapping("") //현재시간, clubID 담아서 보내야함.
    public ResponseEntity<ResponseDTO> regist(@RequestBody RegistAttendanceDTO registAttendanceDTO) throws ParseException {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출석 성공", attendanceService.regist(registAttendanceDTO)));
    }

}
