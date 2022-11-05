package com.habit.thehabit.member.command.domain.aggregate;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TBL_MEMBER")
//@SequenceGenerator(
//        name = "MEMBER_CODE_SEQ_GENERATOR",
//        sequenceName = "MEMBER_CODE_SEQ",
//        initialValue = 1,
//        allocationSize = 1
//)
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "MEMBER_SEQ",
        allocationSize = 1
)

public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
        generator = "MEMBER_SEQ_GENERATOR"
    )
    @Column(name = "MEMBER_CODE")
    private int memberCode;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "MEMBER_PW")
    private String memberPwd;

    @Column(name = "PW_IS_TEMP")
    private String isTempPwd;

    @Column(name = "NICKNAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "SIGNUP_DATE")
    private Date signupDate;

    @Column(name = "WITHDRAWAL_DATE")
    private Date withDrawalDate;

    @Column(name = "IS_WITHDRAWAL")
    private String isWithDrawal;

    @Column(name = "MEMBER_ROLE")
    private String memberRole;


    @Builder
    public Member(int memberCode, String memberId, String memberPwd, String isTempPwd, String name, String phone,
                  Date signupDate, Date withDrawalDate, String isWithDrawal, String memberRole, Collection<? extends GrantedAuthority> authorities) {
        this.memberCode = memberCode;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.isTempPwd = isTempPwd;
        this.name = name;
        this.phone = phone;
        this.signupDate = signupDate;
        this.withDrawalDate = withDrawalDate;
        this.isWithDrawal = isWithDrawal;
        this.memberRole = memberRole;
        this.authorities = authorities;
    }

    /** Security를 위한 코드 */
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
    }

    /** UserDetails를 상속받아서 오버라이딩한 메서드들. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.memberPwd;
    }

    @Override
    public String getUsername() {
        return this.memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberCode=" + memberCode +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", isTempPwd='" + isTempPwd + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", signupDate=" + signupDate +
                ", withDrawalDate=" + withDrawalDate +
                ", isWithDrawal='" + isWithDrawal + '\'' +
                ", memberRole='" + memberRole + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
