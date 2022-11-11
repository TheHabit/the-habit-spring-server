package com.habit.thehabit.club.command.domain.repository;

import com.habit.thehabit.club.command.domain.aggregate.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
}
