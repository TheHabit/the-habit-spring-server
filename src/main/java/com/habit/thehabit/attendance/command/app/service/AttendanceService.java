package com.habit.thehabit.attendance.command.app.service;

import com.habit.thehabit.attendance.command.app.dto.AttendanceInfoDTO;
import com.habit.thehabit.attendance.command.app.dto.RegistAttendanceDTO;
import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.attendance.command.infra.repository.AttendanceInfraRepository;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.MeetingSchedule;
import com.habit.thehabit.club.command.infra.repository.ClubInfraRepository;
import com.habit.thehabit.club.command.infra.repository.MeetingScheduleInfraRepository;
import com.habit.thehabit.club.command.infra.repository.ScheduleInfraRepository;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AttendanceService {

    private final AttendanceInfraRepository attendanceInfraRepository;
    private final MemberInfraRepository memberInfraRepository;
    private final ClubInfraRepository clubInfraRepository;
    private final MeetingScheduleInfraRepository meetingScheduleInfraRepository;

    private final ScheduleInfraRepository scheduleInfraRepository;

    @Autowired
    public AttendanceService(AttendanceInfraRepository attendanceInfraRepository, MemberInfraRepository memberInfraRepository, ClubInfraRepository clubInfraRepository, MeetingScheduleInfraRepository meetingScheduleInfraRepository, ScheduleInfraRepository scheduleInfraRepository) {
        this.attendanceInfraRepository = attendanceInfraRepository;
        this.memberInfraRepository = memberInfraRepository;
        this.clubInfraRepository = clubInfraRepository;
        this.meetingScheduleInfraRepository = meetingScheduleInfraRepository;
        this.scheduleInfraRepository = scheduleInfraRepository;
    }

    /* 출석 체크 */
    public RegistAttendanceDTO regist(RegistAttendanceDTO registAttendanceDTO) throws ParseException {

//        if (isPossible(registAttendanceDTO).getClubName() == null) {
//            return null;
//        }
        /*출석체크하는 사용자의 정보가져오기*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginedMember = (Member) authentication.getPrincipal();
        log.info("loginedMember {}", loginedMember);
        int memberCode = loginedMember.getMemberCode();
        log.info("memberCode : {}", memberCode);

        /*요청한 사용자의 Member entity조회*/
        Member member = memberInfraRepository.findByMemberCode(memberCode);
        log.info("member : {}", member);



        /*출석체크 하려는 Club의 MeetingSchedule을 확인하여 출석 가능한 시간인지 확인*/
        int clubId = registAttendanceDTO.getClubId();
        Club club = clubInfraRepository.findById(clubId);
        LocalDateTime now = LocalDateTime.now();
        MeetingSchedule meetingSchedule = meetingScheduleInfraRepository.findByMeetingDateIsValid(now, clubId);

        /*이미 출석했는지 확인*/
        LocalDateTime meetingTimeDate = meetingSchedule.getMeetingDateTime();
        if(attendanceInfraRepository.checkDuplicated(memberCode, clubId, meetingTimeDate) != null){
            registAttendanceDTO.setAttendancedDate(now);
            registAttendanceDTO.setMemberCode(memberCode);
            registAttendanceDTO.setClubId(clubId);
            registAttendanceDTO.setClubName(club.getClubName());
            return registAttendanceDTO;
        }


        /*사용자가 출석요청한 클럽 아이디를 통해 출석체크를 진행함 */
        /* (= 사용자의 Member , Club 객체를 Attendance 객체에 등록)*/
        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setClub(club);
        attendance.setAttendanceDate(now);
        attendance.setMeetingDateTime(meetingSchedule.getMeetingDateTime());
        attendanceInfraRepository.save(attendance);

        /*DTO 반환*/
        registAttendanceDTO.setAttendancedDate(now);
        registAttendanceDTO.setMemberCode(memberCode);
        registAttendanceDTO.setClubId(clubId);
        registAttendanceDTO.setClubName(club.getClubName());
        return registAttendanceDTO;
    }

    /*조회*/
    public List<AttendanceInfoDTO> findAllAttendance(int clubId) {
        List<Attendance> attendanceList = attendanceInfraRepository.findAllByClubId(clubId);
        List<AttendanceInfoDTO> resultList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            resultList.add(attendance.toDTO());
        }

        return resultList;
    }
}

    /*출석 조회시, 출석률 관련 메소드*/


//    /*출석체크시, 예외 처리 메소드*/
//    public RegistAttendanceDTO isPossible(RegistAttendanceDTO registAttendanceDTO) throws ParseException {
//        /*출석 가능한 시간인지 확인*/
//        //출석 여청 들어온 시간(현재시간) 변수에 저장
//        //년/월/일, 요일, 시간 분리하여 변수에 저장
//
//        LocalDate currLDate = LocalDate.now(); //현재날짜
//        Date currDate = java.sql.Date.valueOf(currLDate);
//
//        LocalTime currLTime = LocalTime.now(); //현재시간
//        System.out.println(currLTime);
//        Date currTime = java.sql.Date.valueOf(String.valueOf(currLTime));
//
//        DayOfWeek DOF = currLDate.getDayOfWeek();
//        String dayOfWeek = DOF.getDisplayName(TextStyle.SHORT, Locale.KOREAN); //현재 요일
//
//        System.out.println("currDate : " + currDate);
//        System.out.println("currTime : " + currLTime);
//        System.out.println("dayOfweek : " + dayOfWeek);
//
//        /* 현재 날짜가, 출석 가능한 날짜인지 확인*/
//        int clubId = registAttendanceDTO.getClubId();
//        Club club = clubInfraRepository.findById(clubId);
//
//        Date startDate = club.getPeriod().getStartDate();//모임 시작일
//        Date endDate = club.getPeriod().getEndDate();//모임 종료일
//
//        if ((currDate.equals(startDate) || currDate.after(startDate)) && (currDate.before(endDate) || currDate.equals(endDate))) {
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//            Calendar calendar = Calendar.getInstance();
//
//            /* 해당 요일(dayOfWeek)이 있는지 확인 , true -> 출석 가능한 시간인지 확인 ,true -> 출석로직 진행*/
//            List<Schedule> scheduleList = scheduleInfraRepository.findByDayOfWeek(dayOfWeek);
//            for (Schedule schedule : scheduleList) {
//                //해당되는 요일이 있을 경우, 출석가능한 시간인지 확인
//                if (schedule.getDayOfWeek().contains(dayOfWeek)) {
//                    Date checkStartTime = schedule.getStartTime(); //출석이 가능한 시작 시간
//                    System.out.println("출석 시작 시간: " + checkStartTime);
//
//                    calendar.setTime(checkStartTime); //10분 더한 출석 종료 시간
//                    calendar.add(Calendar.MINUTE, 60); //10분 더한 출석 종료 시간
//                    String checkEndTimeTemp = simpleDateFormat.format(calendar.getTime());
//                    Date checkEndTime = simpleDateFormat.parse(checkEndTimeTemp);
//
//                    System.out.println("출석 종료 시간 String: " + checkEndTimeTemp);
//                    System.out.println("출석 종료 시간 Date: " + checkEndTime);
//
//                    /*현재 시각이 출석가능 시간에 있으면 출석 진행*/
//                    if ((currTime.equals(checkStartTime) || currTime.after(checkStartTime)) && (currTime.before(checkEndTime) || currTime.equals(checkEndTime))) {
//                        registAttendanceDTO.setClubId(clubId);
//                        registAttendanceDTO.setClubName(club.getClubName());
//                        return registAttendanceDTO;
//                    }
//                }
//            }
//        }
//            return registAttendanceDTO;
//
//    }
//
//}



/*==========아래는 확인용===========*/
//해당 요일이 있는지 확인
//        List<Schedule> scheduleList = scheduleInfraRepository.findByDayOfWeek(dayOfWeek);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//        Calendar calendar = Calendar.getInstance();

//        /*확인용 출력문*/
//        for(Schedule schedule : scheduleList){
//            System.out.println("======================");
//            Date checkStartTime = schedule.getStartTime(); //출석이 가능한 시작 시간
//            System.out.println("출석 시작 시간 : " + checkStartTime);
//
//
//            calendar.setTime(checkStartTime); //10분 더한 출석 종료 시간
//            calendar.add(Calendar.MINUTE, 10); //10분 더한 출석 종료 시간
//            String checkEndTime = simpleDateFormat.format(calendar.getTime());
//            System.out.println(" 출석 종료 시간 : " + calendar.getTime()); //
//            System.out.println(" 출석 종료 시간 타입 : " + calendar.getTime().getClass().getName());
//            System.out.println(" 출석 종료 시간 변환 후, : " + checkEndTime); //
//            System.out.println(" 출석 종료 시간 타입 변환 후,: " + checkEndTime.getClass().getName());
//        }

//        /*TEST 현재날짜가 출석기간에 속하는지 TEST*/
//        System.out.println("clubId" + clubId);
//        System.out.println("curDate: " + currDate);
//        System.out.println("startDate: " + startDate);
//        System.out.println("endDate: " + endDate);
//        System.out.println("시작일== "+currDate.equals(startDate));
//        System.out.println("시작일 이후" +currDate.after(startDate));
//        System.out.println("종료일 ==" + currDate.equals(startDate));
//        System.out.println("종료일 이전" + currDate.after(startDate));
