package com.habit.thehabit.member.command.app.service;

import com.habit.thehabit.common.command.app.dto.TokenDTO;
import com.habit.thehabit.config.jwt.TokenProvider;
import com.habit.thehabit.member.command.app.dto.MemberDTO;
import com.habit.thehabit.member.command.app.dto.UpdateRequestDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public TokenDTO updateMember(UpdateRequestDTO updateRequestDTO) {
        log.info("[MemberService] updateMember START ==========================");
        log.info("[MemberService] updateMember {}", updateRequestDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDTO loginedMember = (MemberDTO) authentication.getPrincipal();
        log.info("[MemberService] loginedMember {}", loginedMember);
        if(!passwordEncoder.matches(updateRequestDTO.getMemberPwd(), loginedMember.getMemberPwd())){
            log.info("[MemberService] 비밀번호가 일치하지 않습니다!");
//            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
//        MemberDTO updateMember = new MemberDTO();
//        updateMember.setName(updateMember.getName());
//        updateMember.setPhone(updateMember.getPhone());
//        updateMember.setMemberId(updateMember.getMemberId());

        Member currMember = memberInfraRepository.findByMemberId(loginedMember.getMemberId());
        log.info("[가져온 값 확인] {}", currMember);

        currMember.setName(updateRequestDTO.getName());
        currMember.setMemberId(updateRequestDTO.getMemberId());
        currMember.setPhone(updateRequestDTO.getPhone());
        currMember.setMemberPwd(passwordEncoder.encode(updateRequestDTO.getMemberPwd()));


        log.info("[수정된 내용확인] {}", loginedMember);

        TokenDTO token = tokenProvider.generateTokenDTO(currMember);

        return token;
    }
}
