package com.habit.thehabit.friend.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.friend.app.service.FriendService;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/friends")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    /* 추천친구 목록 조회하기 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getRecommendedFriends(@AuthenticationPrincipal Member member){
        try {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "추천 친구 조회 API 성공", friendService.getRecommendedFriends(member)));
        }catch (RecordNotFoundException e){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "추천 친구 조회 API 조회 실패", "조회실패"));
        }
    }

    /* AI서버로 새로작성된 Record 정보 (memberCode, rank, isbn) 전송 */
    @Scheduled(cron = "00 00 4 * * *")//-매일- 04시에 보내짐 - 순서대로 초,분,시간,일,월,요일 -
    @PostMapping("")
    public ResponseEntity<ResponseDTO> sendDataToAiServer(){
        System.out.println("test");
        try{
//            System.out.println(ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "요청 성공", friendService.sendDataToAiServer())));
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "요청 성공", friendService.sendDataToAiServer()));
        }catch (Exception e){
            System.out.println("에러 발생" + e.getMessage());
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "요청 실패 ", "실패"));
        }
    }
}
