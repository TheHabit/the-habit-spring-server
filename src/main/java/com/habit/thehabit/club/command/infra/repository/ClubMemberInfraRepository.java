package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubMemberRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubMemberInfraRepository extends ClubMemberRepository {

    @Query("select c from ClubMember c where c.club.id = :memberCode AND c.member.memberCode = :clubId")
    ClubMember findByClubIdAndMemberCode(@Param("memberCode") int memberCode, @Param("clubId")int clubId);
    @Query("select c from ClubMember c where c.club.id = :memberCode AND c.member.memberCode = :clubId AND c.withdrawDate IS null")
    ClubMember findByClubIdAndMemberCodeIsValid(@Param("memberCode") int memberCode, @Param("clubId")int clubId);
    List<ClubMember> findAll();

    List<ClubMember> findByClubId(int clubId);

//    void save(Club club);
}
