package com.habit.thehabit.attendance.command.domain.repository;

import com.habit.thehabit.attendance.command.domain.aggregate.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
}
