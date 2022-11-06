package com.habit.thehabit.club.command.domain.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember,Integer> {
}
