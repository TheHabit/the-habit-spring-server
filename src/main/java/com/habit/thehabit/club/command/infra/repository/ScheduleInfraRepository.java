package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.Schedule;
import com.habit.thehabit.club.command.domain.repository.ScheduleRepository;

import java.util.List;

public interface ScheduleInfraRepository extends ScheduleRepository {
    List<Schedule> findByDayOfWeek(String day);
}
