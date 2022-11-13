package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.MeetingSchedule;
import com.habit.thehabit.club.command.domain.repository.MeetingScheduleRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingScheduleInfraRepository extends MeetingScheduleRepository {
    @Query("select m from MeetingSchedule m where :now Between m.meetingDateTime And m.attendanceEndTime And m.club.id = :clubId")
    MeetingSchedule findByMeetingDateIsValid(@Param("now") LocalDateTime now, @Param("clubId") int clubId);


    List<MeetingSchedule> findAllByMeetingDateTimeBeforeAndClubId(LocalDateTime now,int clubId);

//    @Query("select m from MeetingSchedule m where m.club.id = : clubId And m.meetingDateTime ")
//    List<MeetingSchedule> findAllByMeetingScheduleByBeforeCurrentDate(@Param("now") LocalDateTime now, @Param("clubId") int clubId);
}
