package com.habit.thehabit.club.command.app.controller;

import com.habit.thehabit.club.command.app.dto.CreateClubDTO;
import com.habit.thehabit.club.command.app.dto.JoinClubDTO;
import com.habit.thehabit.club.command.app.dto.WithdrawDTO;
import com.habit.thehabit.club.command.app.service.ClubService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.target.command.app.controller.TargetController;
import com.habit.thehabit.target.command.app.service.UploadToS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/v1/clubs")
@Slf4j
public class ClubController {

    private final ClubService clubService;

    private final UploadToS3 uploadToS3;
    @Autowired
    public ClubController(ClubService clubService, UploadToS3 uploadToS3) {
        this.clubService = clubService;
        this.uploadToS3 = uploadToS3;
    }

    /* club 개설  */
    /*모임 일정 생성 후, 작업중 2022-11-09*/
//    @PostMapping("")
//    public ResponseEntity<ResponseDTO> createClub(@RequestBody CreateClubDTO createClubDTO){
//        System.out.println("클럽 개설 확인");
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(createClubDTO)));
//    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDTO> createClub( @RequestPart CreateClubDTO request, @RequestPart MultipartFile imgFile ) throws IOException {
        System.out.println("클럽 개설 확인");
        String imageUri = uploadToS3.upload(imgFile,"club");
        request.setImageUri(imageUri);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
    }


    /* 개설된 club목록 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getClubs(){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회요청 성공", clubService.findAllClubs()));
    }

    /* club 참가 신청*/
    @PutMapping("")
    public ResponseEntity<ResponseDTO> joinClub(@RequestBody JoinClubDTO joinClubDTO){
        System.out.println("joinClub 요청확인");
        int clubId = joinClubDTO.getClubId();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "신청요청 성공", clubService.joinClub(clubId)));
    }


    /* club 탈퇴 */
    @PatchMapping("")
    public ResponseEntity<ResponseDTO> withdrawClub(@RequestBody WithdrawDTO withdrawDTO ){

        System.out.println("컨트롤러 클럽 탈퇴 요청 확인");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "클럽 탈퇴 성공", clubService.withdrawClub(withdrawDTO)));
    }


}
