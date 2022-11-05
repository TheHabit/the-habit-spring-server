package com.habit.thehabit.common.command.app.service;

import com.habit.thehabit.common.command.app.exception.UserNotFoundException;
import com.habit.thehabit.member.command.app.dto.MemberDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberInfraRepository memberInfraRepository;

    public CustomUserDetailsService(MemberInfraRepository memberInfraRepository){
        this.memberInfraRepository = memberInfraRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        log.info("[CustomUserDetailsService] ===================================");
        log.info("[CustomUserDetailsService] loadUserByUsername {}", memberId);

        Member member =  memberInfraRepository.findByMemberId(memberId);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getMemberRole());
        System.out.println("grantedAuthority = " + grantedAuthority);
        
        Collection<? extends GrantedAuthority> collection = Arrays.asList(grantedAuthority);
        System.out.println("collection = " + collection);
//        member.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(member.getMemberRole())));
        member.setAuthorities(collection);
        System.out.println("memberRole authorities : " + member.getAuthorities());

        if(member == null){
            throw new UsernameNotFoundException(memberId + " 회원 정보가 존재하지 않습니다.");
        }

        System.out.println("member = " + member);

        log.info("[CustomUserDetailsService] END ===================================");
        return member;
    }


}
