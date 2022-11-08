package com.habit.thehabit.club.command.app.controller;

import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.app.dto.JoinClubDTO;
import com.habit.thehabit.club.command.app.service.ClubService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/clubs")
@Slf4j
public class ClubController {

    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    /* 개설된 club목록 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getClubs(){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", clubService.findAllClubs()));
    }

    /* club 개설  */
    @PostMapping("")
    public ResponseEntity<ResponseDTO> createClub(@RequestBody ClubDTO clubDTO){
        System.out.println("클럽 개설 요청 확인");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성 성공", clubService.createClubs(clubDTO)));
    }

    /* club 참가 신청*/
    @PutMapping("")
    public ResponseEntity<ResponseDTO> joinClub(@RequestBody JoinClubDTO joinClubDTO){
        System.out.println("joinClub 요청확인");
        int clubId = joinClubDTO.getClubId();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "신청 성공", clubService.joinClub(clubId)));
    }


}