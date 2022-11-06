package com.habit.thehabit.club.command.domain.repository;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Integer> {

}
