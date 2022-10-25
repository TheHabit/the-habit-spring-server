package com.habit.thehabit.app.service;

import com.habit.thehabit.app.dao.repository.MemberRepository;
import com.habit.thehabit.app.dao.entity.Member;
import com.habit.thehabit.app.dto.TokenDTO;
import com.habit.thehabit.config.jwt.TokenProvider;
import com.habit.thehabit.exception.DuplicateIdException;
import com.habit.thehabit.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, TokenProvider tokenProvider, MemberRepository memberRepository){
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member signup(Member member){

        /** id 중복 여부 체크 */
        if( memberRepository.findByMemberId(member.getMemberId()) != null){
            throw new DuplicateIdException("중복된 ID가 존재합니다.");
        }

        /** 비밀번호 encoding 해서 setting */
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
        System.out.println("member.getMemberPwd() = " + member.getMemberPwd() + "|");

        /** insert 후 결과 반환 */
        memberRepository.save(member);

        return memberRepository.findByMemberId(member.getMemberId());
    }

    @Transactional
    public TokenDTO login(Member member) throws LoginFailedException {

        System.out.println("member.getMemberPwd() = " + member.getMemberPwd());
        /** 1. 아이디 조회 */
        Member foundMember = memberRepository.findByMemberId(member.getMemberId());
        System.out.println("foundMember.getMemberPwd() = " + foundMember.getMemberPwd());

        if(foundMember == null){
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
}
