package com.habit.thehabit.attendance.command.app.controller;

import com.habit.thehabit.attendance.command.app.dto.AttendanceInfoDTO;
import com.habit.thehabit.attendance.command.app.dto.RegistAttendanceDTO;
import com.habit.thehabit.attendance.command.app.service.AttendanceService;
import com.habit.thehabit.club.command.app.dto.CreateClubDTO;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Tag(name = "attendances",description = "출석API")
@RestController
@RequestMapping("/v1/attendances")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Operation(summary = "출석 조회 API", description = "출석 내역을 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출석 조회 성공",
                    content = @Content(schema = @Schema(implementation = AttendanceInfoDTO.class)))})

    /* 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> findAllAttendance(@RequestParam(value = "clubId", defaultValue = "")int clubId){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", attendanceService.findAllAttendance(clubId)));
    }

    /* 출석 */
    @PostMapping("")
    public ResponseEntity<ResponseDTO> regist(@RequestBody RegistAttendanceDTO registAttendanceDTO) throws ParseException {
       try {
           return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출석 성공", attendanceService.regist(registAttendanceDTO)));
       }catch (NullPointerException e){
           return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출석이 불가능합니다.", null));
       }
    }

}
