package com.habit.thehabit.club.command.app.service;

import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.infra.repository.ClubInfraRepository;
import com.habit.thehabit.club.command.infra.repository.ClubMemberInfraRepository;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ClubService {
    private final ClubInfraRepository clubInfraRepository;
    private final MemberInfraRepository memberInfraRepository;

    private final ClubMemberInfraRepository clubMemberInfraRepository;

    @Autowired
    public ClubService(ClubInfraRepository clubInfraRepository, MemberInfraRepository memberInfraRepository, ClubMemberInfraRepository clubMemberInfraRepository) {
        this.clubInfraRepository = clubInfraRepository;
        this.memberInfraRepository = memberInfraRepository;
        this.clubMemberInfraRepository = clubMemberInfraRepository;
    }

    public List<Club> findAllClubs() {
        List<Club> clubList = clubInfraRepository.findAll();
        return clubList;
    }

    public Club createClubs(ClubDTO clubDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
//        System.out.println("authentication.getPrincipal :" + authentication.getPrincipal());
        int memberCode = loginedMember.getMemberCode();


        Club club = new Club();
        /* +++방 만든사람 추가해줘야하는데 키를 가진쪽이 아님, 메소드 생성해줘야함 ->편의 메소드?*/
        club.setClubName(clubDTO.getClubName());
        club.setBookName(club.getBookName());
        club.setRecruitPeriod(clubDTO.getRecruitStartDate(),clubDTO.getRecruitEndDate());
        club.setPeriod(clubDTO.getStartDate(),clubDTO.getEndDate());
        clubInfraRepository.save(club);

        /*방금DB에 생성된 pk값을 알수있나..?*/

        return club;
    }

    public Club joinClub() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
//        System.out.println("authentication.getPrincipal :" + authentication.getPrincipal());
        int memberCode = loginedMember.getMemberCode();

        return null;
    }
}
