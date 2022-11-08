package com.habit.thehabit.common.command.app.service;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.common.command.app.dto.TokenDTO;
import com.habit.thehabit.config.jwt.TokenProvider;
import com.habit.thehabit.common.command.app.exception.DuplicateIdException;
import com.habit.thehabit.common.command.app.exception.LoginFailedException;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberInfraRepository memberInfraRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, TokenProvider tokenProvider, MemberInfraRepository memberInfraRepository){
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.memberInfraRepository = memberInfraRepository;
    }

    @Transactional
    public TokenDTO login(Member member) throws LoginFailedException {

        System.out.println("member.getMemberPwd() = " + member.getMemberPwd());
        Member foundMember;
        /** 1. 아이디 조회 */
        try{
            System.out.println("member.getMemberId() = " + member.getMemberId());
//            foundMember = memberInfraRepository.findByMemberId(member.getMemberId());
            foundMember = memberInfraRepository.findByMemberIdAndIsWithDrawal(member.getMemberId(),"N");
            System.out.println("foundMember.getMemberPwd() = " + foundMember.getMemberPwd());
        } catch(Exception e){
            System.out.println("e = " + e);
            throw new LoginFailedException("아이디가 존재하지 않습니다.");
        }


        /** 2. 비밀번호 체크 */
        if(!passwordEncoder.matches(member.getMemberPwd(), foundMember.getMemberPwd())){
            System.out.println("foundMember = " + foundMember.getMemberPwd() + "|");
            System.out.println("member = " + member.getMemberPwd() + "|");
            throw new LoginFailedException("비밀번호가 다릅니다.");
        }

        /** 3. 토큰 발급 */
        TokenDTO token = tokenProvider.generateTokenDTO(foundMember);

        return token;
    }

    public Member update(Member member, User user) {

        /** 회원 정보 조회 */
        System.out.println("member = " + member);

        return null;
    }
}
