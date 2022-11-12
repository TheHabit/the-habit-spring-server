package com.habit.thehabit.club.command.domain.repository;

import com.habit.thehabit.club.command.domain.aggregate.MeetingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule,Integer> {
}
