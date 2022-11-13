package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubMemberRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClubMemberInfraRepository extends ClubMemberRepository {

    @Query("select c from ClubMember c where c.club.id = :clubId  AND c.member.memberCode = :memberCode order by c.id")
    ClubMember findByClubIdAndMemberCode(@Param("memberCode") int memberCode, @Param("clubId")int clubId);
    @Query("select c from ClubMember c where c.club.id = :clubId AND c.member.memberCode = :memberCode AND c.withdrawDate IS null order by c.id")
    ClubMember findByClubIdAndMemberCodeIsValid(@Param("memberCode") int memberCode, @Param("clubId")int clubId);

    List<ClubMember> findAll();

//    List<ClubMember> findByClubId(int clubId);

//    void save(Club club);
}
