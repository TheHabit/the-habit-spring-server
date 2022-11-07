package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubMemberRepository;

import java.util.List;

public interface ClubMemberInfraRepository extends ClubMemberRepository {
    List<ClubMember> findAll();
    List<ClubMember> findByClubId(int clubId);


//    void save(Club club);
}
