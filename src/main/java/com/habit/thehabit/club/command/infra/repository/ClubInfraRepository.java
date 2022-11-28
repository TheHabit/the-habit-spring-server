package com.habit.thehabit.club.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.club.command.domain.repository.ClubRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface ClubInfraRepository extends ClubRepository {

    @Query("select c.club from ClubMember c where c.member.memberCode =:memberCode And c.withdrawDate Is null order by c.id")
    List<Club> findClubMembersByMemberCodeIsValid(int memberCode);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    List<Club> findAll();

//    @Query("select c.id, c.clubName, c.clubIntro, c.currentNumberOfMember, c.bookName  from Club c")
//    List<Club> findAllClub();

    @Query("select c.club from ClubMember c where c.club.recruitPeriod.endDate > :now and c.club.currentNumberOfMember > 0")
    List<Club> findClubByRecruitEndDateBefore(LocalDateTime now);

    Club findById(int id);
}
