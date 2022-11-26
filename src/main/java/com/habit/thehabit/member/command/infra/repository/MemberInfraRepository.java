package com.habit.thehabit.member.command.infra.repository;

import com.habit.thehabit.club.command.domain.aggregate.ClubMember;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.domain.repository.MemberRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface MemberInfraRepository extends MemberRepository {

    Member findByMemberId (String memberId);

    Member findByMemberIdAndIsWithDrawal (String memberId, String isWithDrawal);

    Member findByMemberCode(int memberCode);

}
