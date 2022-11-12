package com.habit.thehabit.attendance.command.infra.repository;

import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.attendance.command.domain.repository.AttendanceRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceInfraRepository extends AttendanceRepository {
    List<Attendance> findAllByClubId(int clubId);

    @Query("select a from Attendance a where a.meetingDateTime = :meetingDateTime And a.member.memberCode = :memberCode And a.club.id = :clubId")
    Attendance checkDuplicated(@Param("memberCode") int memberCode, @Param("clubId") int clubId, @Param("meetingDateTime") LocalDateTime meetingDateTime);
}
