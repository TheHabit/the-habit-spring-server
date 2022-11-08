package com.habit.thehabit.attendance.command.app.service;

import com.habit.thehabit.attendance.command.app.dto.RegistAttendanceDTO;
import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.attendance.command.infra.repository.AttendanceInfraRepository;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.infra.repository.ClubInfraRepository;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceService {

    private final AttendanceInfraRepository attendanceInfraRepository;
    private final MemberInfraRepository memberInfraRepository;

    private final ClubInfraRepository clubInfraRepository;
    @Autowired
    public AttendanceService(AttendanceInfraRepository attendanceInfraRepository, MemberInfraRepository memberInfraRepository, ClubInfraRepository clubInfraRepository) {
        this.attendanceInfraRepository = attendanceInfraRepository;
        this.memberInfraRepository = memberInfraRepository;
        this.clubInfraRepository = clubInfraRepository;
    }

    public RegistAttendanceDTO regist(RegistAttendanceDTO registAttendanceDTO) {
        /*출석체크하는 사용자의 정보가져오기*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);

        /*요청한 사용자의 Member entity조회*/
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);

        /*출석체크 하려는 Club의 entity를 조회함*/
        int clubId = registAttendanceDTO.getClubId();
        Club club = clubInfraRepository.findById(clubId);

        /*사용자가 출석요청한 날짜,클럽 아이디를 통해 출석체크를 진행함 */
        /* (= 사용자의 Member , Club 객체를 Attendance 객체에 등록)*/
        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setClub(club);
        attendance.setAttendanceDate(registAttendanceDTO.getDate()); // dto에 담겨있는 출석체크시간을 담음
        attendanceInfraRepository.save(attendance);

        /*DTO 반환*/
        registAttendanceDTO.setMemberCode(memberCode);
        registAttendanceDTO.setClubId(clubId);
        registAttendanceDTO.setClubName(club.getClubName());

        return registAttendanceDTO;
    }
}
