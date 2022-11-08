package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.repository.ClubRepository;

import java.util.List;

public interface ClubInfraRepository extends ClubRepository {
    List<Club> findAll();
    Club findById(int id);
}
