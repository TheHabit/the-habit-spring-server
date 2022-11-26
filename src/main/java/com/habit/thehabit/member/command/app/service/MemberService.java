package com.habit.thehabit.member.command.app.service;

import com.habit.thehabit.common.command.app.dto.TokenDTO;
import com.habit.thehabit.common.command.app.exception.DuplicateIdException;
import com.habit.thehabit.config.jwt.TokenProvider;
import com.habit.thehabit.member.command.app.dto.MemberAdminDTO;
import com.habit.thehabit.member.command.app.dto.MemberDTO;
import com.habit.thehabit.member.command.app.dto.MemberListAndSizeDTO;
import com.habit.thehabit.member.command.app.dto.UpdateRequestDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Arrays;

import java.util.Date;
import java.util.List;

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

    /*더미 데이터 생성할 때 사용*/
//    @Transactional
//    public String signuptemp(Member member){
//        /* ------------------------------------ */
//        String names =
//                    "홍믿음, 임미르, 강버들, 송나길, 조나라우람, 손다운, 홍힘찬, 예한길, 고샘, 황힘찬, 설다운, 제갈빛가람, 한힘찬, 백달, 류빛가람, 최한길, 신나라우람, 안나라우람, 홍우람, 복나길"+
//                ", "+
//                    "최동수, 제갈태석, 설진환, 손경진, 김민수, 복규빈, 류경하, 윤승환, 윤병일, 제갈수근, 장혜빈, 하윤정, 서혜빈, 안윤지, 신연주, 성효진, 양진우, 임민태, 홍윤호, 손재남"+
//                ", "+
//                    "남궁빛나, 안우리, 탁두리, 복다래, 장새론, 조보라, 정한별, 백솔, 사공햇빛, 신나봄, 제갈봄, 김하늬, 장하다, 정나리, 성라온, 하고은, 전별빛, 제갈두리, 손아롱, 이나봄"+
//                ", "+
//                    "고웅, 전훈, 류광, 문광, 사공광, 권광, 송훈, 제갈광, 안훈, 풍광, 하건, 황보웅, 백건, 서건, 장웅, 신철, 황보건, 정광, 오호, 신건"+
//                ", "+"류린, 손은, 문진, 예현, 황지, 고은, 황보성, 사공란, 권설, 김성, 홍진, 손리, 표성, 허성, 송란, 강현, 허설, 신상, 탁린, 박지";
//
//        List<String> nameList = Arrays.asList(names.split(", "));
//        System.out.println(nameList);
//        String id = "remate";
//
//        /* ------------------------------------ */
//        for(int i = 0 ; i < 100; i++) {
//            Member newMember = new Member();
//            newMember.setMemberId(id + i);
//            newMember.setMemberPwd(id + i);
//            newMember.setName(nameList.get(i));
//
//            /** id 중복 여부 체크 */
//            if (memberInfraRepository.findByMemberId(newMember.getMemberId()) != null) {
//                throw new DuplicateIdException("중복된 ID가 존재합니다.");
//            }
//
//            /** 비밀번호 encoding 해서 setting */
//            newMember.setMemberPwd(passwordEncoder.encode(newMember.getMemberPwd()));
//            System.out.println("newmember.getMemberPwd() = " + newMember.getMemberPwd() + "|");
//
//            /** insert 후 결과 반환 */
//            memberInfraRepository.save(newMember);
//        }
//        return null;
//    }



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

    public MemberListAndSizeDTO getMembers(Pageable pageable) {
        Page<Member> memberList = memberInfraRepository.findAll(pageable);

        System.out.println("memberList = " + memberList);
        List<MemberAdminDTO> memberAdminDTOList = new ArrayList<>();
        for(Member member : memberList){
            MemberAdminDTO memberAdminDTO = new MemberAdminDTO(member.getMemberCode(), member.getMemberId(),
                    member.getName(), member.getMemberRole(), member.getIsWithDrawal());

            memberAdminDTOList.add(memberAdminDTO);
        }
        System.out.println("memberAdminDTOList = " + memberAdminDTOList);

        MemberListAndSizeDTO memberListAndSizeDTO = new MemberListAndSizeDTO(memberAdminDTOList, memberList.getTotalElements());

        return memberListAndSizeDTO;
    }
}
