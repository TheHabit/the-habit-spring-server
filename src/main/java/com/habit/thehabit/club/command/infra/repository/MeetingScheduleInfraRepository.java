package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.MeetingSchedule;
import com.habit.thehabit.club.command.domain.repository.MeetingScheduleRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MeetingScheduleInfraRepository extends MeetingScheduleRepository {
    @Query("select m from MeetingSchedule m where :now Between m.meetingDateTime and m.attendanceEndTime And m.club.id = :clubId")
    MeetingSchedule findByMeetingDateIsValid(@Param("now") LocalDateTime now, @Param("clubId") int clubId);
}
