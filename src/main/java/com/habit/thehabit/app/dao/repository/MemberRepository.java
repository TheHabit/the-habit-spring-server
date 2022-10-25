package com.habit.thehabit.app.dao.repository;

import com.habit.thehabit.app.dao.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByMemberId (String memberId);
}
