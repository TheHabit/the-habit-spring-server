package com.habit.thehabit.attendance.command.infra.repository;

import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import com.habit.thehabit.attendance.command.domain.repository.AttendanceRepository;

import java.util.List;

public interface AttendanceInfraRepository extends AttendanceRepository {
    List<Attendance> findAllByClubId(int clubId);
}
