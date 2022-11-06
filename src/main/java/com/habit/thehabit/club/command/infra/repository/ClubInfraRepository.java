package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.repository.ClubRepository;

import java.util.List;

public interface ClubInfraRepository extends ClubRepository {
    List<Club> findAll();
}
