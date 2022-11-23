package com.habit.thehabit.friend.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.friend.app.service.FriendService;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @GetMapping("")
    public ResponseEntity<ResponseDTO> getRecommendedFriends(@AuthenticationPrincipal Member member){
        System.out.println("통신 테스트");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "친구추전 API 성공", friendService.getRecommendedFriends(member)));
    }
}
