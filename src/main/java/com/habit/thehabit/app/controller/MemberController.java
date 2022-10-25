package com.habit.thehabit.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/v1/member")
public class MemberController {

    @ResponseBody
    @GetMapping("test")
    public String test(){
        return "MemberTest";
    }

    @GetMapping("login")
    public String goLogin(){
        return "login";
    }

    @ResponseBody
    @GetMapping("success")
    public String success(){
        return "You're successed to login!";
    }

    @ResponseBody
    @GetMapping("fail")
    public String fail(){
        return "You're failed to login!";
    }
}
