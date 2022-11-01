package com.habit.thehabit.common.command.app.service;

import com.habit.thehabit.common.command.app.exception.UserNotFoundException;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberInfraRepository memberInfraRepository;
//
//    public CustomUserDetailsService(MemberInfraRepository memberInfraRepository){
//        this.memberInfraRepository = memberInfraRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
//        Member member =  memberInfraRepository.findByMemberId(memberId);
//
//        try{
//            return addAuthorities(member);
//        } catch (Exception e){
//            throw new UserNotFoundException(memberId + "를 찾을 수 없습니다.");
//        }
//
//    }
//
//    private Member addAuthorities(Member member) {
//        member.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(member.getMemberRole())));
//
//        return member;
//    }
//}
