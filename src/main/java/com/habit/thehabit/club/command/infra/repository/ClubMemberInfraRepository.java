package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubMemberRepository;

import java.util.List;

public interface ClubMemberInfraRepository extends ClubMemberRepository {
    List<ClubMember> findAll();

}
