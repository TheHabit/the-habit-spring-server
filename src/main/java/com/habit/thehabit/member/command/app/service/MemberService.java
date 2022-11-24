package com.habit.thehabit.member.command.app.service;

import com.habit.thehabit.common.command.app.dto.TokenDTO;
import com.habit.thehabit.common.command.app.exception.DuplicateIdException;
import com.habit.thehabit.config.jwt.TokenProvider;
import com.habit.thehabit.member.command.app.dto.UpdateRequestDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberInfraRepository memberInfraRepository;

    private final TokenProvider tokenProvider;

    @Autowired
    public MemberService(PasswordEncoder passwordEncoder, MemberInfraRepository memberInfraRepository, TokenProvider tokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.memberInfraRepository = memberInfraRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public Member signup(Member member){

        /** id 중복 여부 체크 */
        if( memberInfraRepository.findByMemberId(member.getMemberId()) != null){
            throw new DuplicateIdException("중복된 ID가 존재합니다.");
        }

        /** 비밀번호 encoding 해서 setting */
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
        System.out.println("member.getMemberPwd() = " + member.getMemberPwd() + "|");

        /** insert 후 결과 반환 */
        memberInfraRepository.save(member);

        return memberInfraRepository.findByMemberId(member.getMemberId());
    }

//    @Transactional
//    public Member signuptemp(Member member){
//        /* ------------------------------------ */
//        String str = ""
//        /* ------------------------------------ */
//        String[] names = new String []{""};
//        for(int i = 0 ; i < 100; i++){
//            Member newMember = new Member();
//            member.setMemberId();
//            member.setMemberPwd();
//            member.setName();
//        }
//        /** id 중복 여부 체크 */
//        if( memberInfraRepository.findByMemberId(member.getMemberId()) != null){
//            throw new DuplicateIdException("중복된 ID가 존재합니다.");
//        }
//
//        /** 비밀번호 encoding 해서 setting */
//        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
//        System.out.println("member.getMemberPwd() = " + member.getMemberPwd() + "|");
//
//        /** insert 후 결과 반환 */
//        memberInfraRepository.save(member);
//
//        return memberInfraRepository.findByMemberId(member.getMemberId());
//    }
//


    @Transactional
    public TokenDTO updateMember(UpdateRequestDTO updateUser) {
        log.info("[MemberService] updateMember START ==========================");
        log.info("[MemberService] updateMember {}", updateUser);

        /** authentication을 이용해 userdetails 정보 가져옴 */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("[MemberService] loginedMember {}", loginedMember);
        System.out.println("authentication.getPrincipal :" + authentication.getPrincipal());

        /** 정보 수정 전 비밀번호 확인을 통해 유저가 맞는지 재차 확인 -> 추후 컨트롤러 분리 */
//        if(!passwordEncoder.matches(updateRequestDTO.getMemberPwd(), loginedMember.getMemberPwd())){
//            log.info("[MemberService] 비밀번호가 일치하지 않습니다!");
//            throw new PasswordException("비밀번호가 일치하지 않습니다.");
//        }

        /** 영속성 컨텍스트에서 로그인되어있는 회원 정보 가져오기 */
        Member currMember = memberInfraRepository.findByMemberCode(loginedMember.getMemberCode());
        log.info("[MemberService] 영속성 컨텍스트에서 가지고 온 값 확인 : ", currMember);

        /** 파라미터로 넘어온 값들이 null이 아닐 경우에만 수정하고자 하는 것으로 판단하여 수정. */
        if(updateUser.getName() != null){
            currMember.setName(updateUser.getName());
        }
        if(updateUser.getMemberPwd() != null){
            currMember.setMemberPwd(passwordEncoder.encode(updateUser.getMemberPwd()));
        }
        if(updateUser.getPhone() != null){
            currMember.setPhone(updateUser.getPhone());
        }

        /** 새로운 유저 정보가 포함된 토큰 반환 */
        TokenDTO token = tokenProvider.generateTokenDTO(currMember);

        return token;
    }

    public Object deleteMember() {
        /** authentication을 이용해 userdetails 정보 가져옴 */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("[MemberService] loginedMember {}", loginedMember);
        System.out.println("authentication.getPrincipal :" + authentication.getPrincipal());
        System.out.println(loginedMember.getMemberCode());
        int memberCode = loginedMember.getMemberCode();

        /* member의 isWithrawal 상태 변경*/
        Date currTime = DateTime.now().toDate();
        System.out.println(currTime);
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        member.setIsWithDrawal("Y");
        member.setWithDrawalDate(currTime);

        memberInfraRepository.save(member);
        return null;
    }
}
