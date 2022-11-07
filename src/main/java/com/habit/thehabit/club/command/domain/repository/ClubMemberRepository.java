package com.habit.thehabit.club.command.domain.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember,Integer> {
}
