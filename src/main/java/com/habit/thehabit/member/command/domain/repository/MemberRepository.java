package com.habit.thehabit.member.command.domain.repository;

import com.habit.thehabit.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
