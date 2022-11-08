package com.habit.thehabit.club.command.app.service;
import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.app.dto.ClubMemberDTO;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
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
import java.util.ArrayList;
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

    public List<ClubDTO> findAllClubs() {
        List<Club> clubList = clubInfraRepository.findAll();

        List<ClubDTO> clubDTOList = new ArrayList<>();
        for(Club club : clubList){
            clubDTOList.add(club.toClubDTO());
        }
        return clubDTOList;
    }

    public ClubDTO createClubs(ClubDTO clubDTO) {

        /*club을 생성하려는 사용자의 정보가져오기*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);

        System.out.println(clubDTO);
        /*받아온 ClubDTO를 Club에 담기*/
        Club club = new Club();

        /* +++방 만든사람 추가해줘야하는데 키를 가진쪽이 아님, 메소드 생성해줘야함 ->편의 메소드?*/
        club.setClubName(clubDTO.getClubName());
        club.setBookName(clubDTO.getBookName());
        club.setRecruitPeriod(new Period(clubDTO.getRecruitStartDate(), clubDTO.getRecruitEndDate()));
        club.setPeriod(new Period(clubDTO.getStartDate(), clubDTO.getEndDate()));

        System.out.println("recruitPeriod : " + club.getRecruitPeriod());
        /*ClubMember Entity생성 후 Member, Club 저장*/
        ClubMember clubMember = new ClubMember();
        clubMember.setClub(club);
        clubMember.setMember(member);

        /*DB에 저장*/
        clubMemberInfraRepository.save(clubMember);
        int clubId = clubMember.getClub().getId();
        clubDTO.setClubId(clubId);
        return clubDTO;
    }

    public ClubDTO joinClub(int clubId) {
        System.out.println("joinClub Service 요청확인");

        /* 지원한 사람의 정보 조회*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);

        /* clubID를 통해 유저가 지원하는 Club 조회 */
        List<ClubMember> clubMemberList = clubMemberInfraRepository.findByClubId(clubId);
        Club club = clubMemberList.get(0).getClub();

        /*데이터 베이스에 반영*/
        ClubMember newClubMember = new ClubMember();
        newClubMember.setClub(club);
        newClubMember.setMember(member);
        clubMemberInfraRepository.save(newClubMember);

        /* clubMember entity to DTO */
        ClubDTO clubDTO = club.toClubDTO();
        return clubDTO;
    }
}
