package com.habit.thehabit.member.command.infra.repository;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.domain.repository.MemberRepository;

public interface MemberInfraRepository extends MemberRepository {
    Member findByMemberId (String memberId);

    Member findByMemberCode(int memberCode);
}
