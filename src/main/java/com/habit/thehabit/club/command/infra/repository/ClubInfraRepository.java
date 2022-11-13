package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ClubInfraRepository extends ClubRepository {

    @Query("select c.club from ClubMember c where c.member.memberCode =:memberCode And c.withdrawDate Is null order by c.id")
    List<Club> findClubMembersByMemberCodeIsValid(int memberCode);
    List<Club> findAll();

    @Query("select c.club from ClubMember c where c.club.recruitPeriod.endDate > :now")
    List<Club> findClubByRecruitEndDateBefore(LocalDateTime now);
    Club findById(int id);
}
