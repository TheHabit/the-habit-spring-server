package com.habit.thehabit.club.command.app.service;
import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.app.dto.CreateClubDTO;
import com.habit.thehabit.club.command.app.dto.ScheduleDTO;
import com.habit.thehabit.club.command.app.dto.WithdrawDTO;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.aggregate.embeddable.Period;
import com.habit.thehabit.club.command.infra.repository.ClubInfraRepository;
import com.habit.thehabit.club.command.infra.repository.ClubMemberInfraRepository;
import com.habit.thehabit.club.command.infra.repository.ScheduleInfraRepository;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ClubService {
    private final ClubInfraRepository clubInfraRepository;
    private final MemberInfraRepository memberInfraRepository;
    private final ClubMemberInfraRepository clubMemberInfraRepository;

    private final ScheduleInfraRepository scheduleInfraRepository;

    @Autowired
    public ClubService(ClubInfraRepository clubInfraRepository, MemberInfraRepository memberInfraRepository, ClubMemberInfraRepository clubMemberInfraRepository, ScheduleInfraRepository scheduleInfraRepository) {
        this.clubInfraRepository = clubInfraRepository;
        this.memberInfraRepository = memberInfraRepository;
        this.clubMemberInfraRepository = clubMemberInfraRepository;
        this.scheduleInfraRepository = scheduleInfraRepository;
    }

    /*사용자가 참여중인 것만 조회*/
    public  List<ClubDTO> getMyClubs() {
        /*사용자의 정보가져오기*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();


        List<Club> clubList = clubInfraRepository.findClubMembersByMemberCodeIsValid(memberCode);
        List<ClubDTO> clubDTOList = new ArrayList<>();

        for(Club club : clubList){
            clubDTOList.add(club.toClubDTO());
        }
        return clubDTOList;
    }
    public List<ClubDTO> findAllClubs() {
        List<Club> clubList = clubInfraRepository.findAll();
        List<ClubDTO> clubDTOList = new ArrayList<>();

        for(Club club : clubList){
            clubDTOList.add(club.toClubDTO());
        }
        return clubDTOList;
    }

    /*ClubMember(Member,Club(ScheduleList))??
    * 1. ClubEntity객체에  ScheduleEntity객체를 생성하여 넣은 후 ClubMemberEntity객체에 넣기*/
    public CreateClubDTO createClubs(CreateClubDTO createClubDTO) {

        /*club을 생성하려는 사용자의 정보가져오기*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);

        System.out.println(createClubDTO);


        /*Club club생성하여 DTO에서 필요한 값들 담기*/
        Club club = new Club();
        club.setClubName(createClubDTO.getClubName());
        club.setBookName(createClubDTO.getBookName());
        club.setRecruitPeriod(new Period(createClubDTO.getRecruitStartDate(), createClubDTO.getRecruitEndDate()));
        club.setPeriod(new Period(createClubDTO.getStartDate(), createClubDTO.getEndDate()));
        System.out.println(createClubDTO.getNumberOfMember());
        club.setNumberOfMember(createClubDTO.getNumberOfMember());
        club.setImageUri(createClubDTO.getImageUri());
        club.addCurrentNumberOfMember();

        /* Club 객체에 넣을 List<Schedule>만들기(DTO.list<ScheduleDTO> -> List<Schedulte>)*/
        //List<Schedule> scheduleList = new ArrayList<>();
        for(ScheduleDTO scheduleDTO : createClubDTO.getScheduleDTOList()){
            club.addSchedule(scheduleDTO.toSchedule());
        }

        System.out.println("recruitPeriod : " + club.getRecruitPeriod());
        /*ClubMember Entity생성 후 Member, Club 저장*/

        ClubMember clubMember = new ClubMember();
        clubMember.setClub(club);
        clubMember.setMember(member);

        /*DB에 저장*/
        clubMemberInfraRepository.save(clubMember);
        int clubId = clubMember.getClub().getId();
        createClubDTO.setClubId(clubId);
        System.out.println(createClubDTO);
        return createClubDTO;
    }

    public Object joinClub(int clubId) {
        System.out.println("joinClub Service 요청확인");

        String message = "최대 모집인원을 초과하였습니다.";
        /* 지원한 사람의 정보 조회*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);

        /* clubID를 통해 유저가 지원하는 Club 조회 */
        Club club = clubInfraRepository.findById(clubId);
//        List<ClubMember> clubMemberList = clubMemberInfraRepository.findByClubId(clubId);
//        Club club = clubMemberList.get(0).getClub();

        ClubDTO clubDTO = new ClubDTO();
        /* 이미 클럽에 참여중인 유저가 요청한 경우 예외 처리*/
        if( clubMemberInfraRepository.findByClubIdAndMemberCodeIsValid(clubId,memberCode) != null){
            message = "이미 참가한 모임입니다.";
            clubDTO.setMessage(message);
            return clubDTO;
        }

        /*데이터 베이스에 반영*/
            /*모집인원보다 현재 인원이 적으면 참여신청 가능*/
        if(club.getNumberOfMember() > club.getCurrentNumberOfMember()){
            ClubMember newClubMember = new ClubMember();
            newClubMember.setClub(club);
            newClubMember.setMember(member);
            newClubMember.getClub().addCurrentNumberOfMember(); //인원수 추가
            clubMemberInfraRepository.save(newClubMember);
            message = "참가 신청이 완료되었습니다.";
        }

        /* clubMember entity to DTO */
        clubDTO = club.toClubDTO();
        clubDTO.setMessage(message);
        return clubDTO;
    }

    public WithdrawDTO withdrawClub(WithdrawDTO withdrawDTO ) {
        /* 지원한 사람의 정보 조회*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);
//        Member member = memberInfraRepository.findByMemberCode(memberCode);
//        log.info("member : {}", member);

        /* clubId, memberCode를 통해 ClubMember를 조회 후 삭제 */
        Date currTime = DateTime.now().toDate();
        int clubId = withdrawDTO.getClubId();

        try {
            ClubMember clubMember = clubMemberInfraRepository.findByClubIdAndMemberCodeIsValid(clubId,memberCode);
            clubMember.setWithdrawDate(currTime);
            clubMember.getClub().removeCurrentNumberOfMember();//현재 참가인원수 -1
            withdrawDTO.setClubName(clubMember.getClub().getClubName());
            withdrawDTO.setWithdrawDate(currTime);
            withdrawDTO.setMemberName(clubMember.getMember().getName());
        }catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
        return withdrawDTO;
    }


}
