package com.habit.thehabit.club.command.app.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.habit.thehabit.club.command.app.dto.CreateClubDTO;
import com.habit.thehabit.club.command.app.dto.JoinClubDTO;
import com.habit.thehabit.club.command.app.dto.WithdrawDTO;
import com.habit.thehabit.club.command.app.exception.DuplicationException;
import com.habit.thehabit.club.command.app.exception.OverstaffedException;
import com.habit.thehabit.club.command.app.service.ClubService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.util.UploadToS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
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

    /*club 생성*/
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDTO> createClub( @RequestPart CreateClubDTO request, @RequestPart MultipartFile imgFile ) throws IOException {
        System.out.println("클럽 개설 확인");
        String imageUri = uploadToS3.upload(imgFile,"club");
        request.setImageUri(imageUri);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
    }

    /* 개설된 club목록 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getClubs(@RequestParam(value = "option", defaultValue = "-1")int option){
        if(option == 1){
            //option이 1인 경우 참가한 클럽 조회
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "참여중인 클럽 조회성공",clubService.getMyClubs()));
        }
        if(option == 2){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모집중인 클럽 조회성공", clubService.getRecruitingClubs()));

        }
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모든 클럽 조회성공", clubService.findAllClubs()));
    }

    /* club 참가 신청*/
    @PutMapping("")
    public ResponseEntity<ResponseDTO> joinClub(@RequestBody JoinClubDTO joinClubDTO){
        System.out.println("joinClub 요청확인");
        int clubId = joinClubDTO.getClubId();
        try {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "신청 성공", clubService.joinClub(clubId)));
        } catch (DuplicationException e){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이미 신청된 모임입니다.", null));
        } catch (OverstaffedException e){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모집인원을 초과하여 신청이 불가능합니다.", null));
        }
    }


    /* club 탈퇴 */
    @PatchMapping("")
    public ResponseEntity<ResponseDTO> withdrawClub(@RequestBody WithdrawDTO withdrawDTO ){

        System.out.println("컨트롤러 클럽 탈퇴 요청 확인");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "클럽 탈퇴 성공", clubService.withdrawClub(withdrawDTO)));
    }


}
