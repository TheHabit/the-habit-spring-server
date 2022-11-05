package com.habit.thehabit.club.command.domain.aggregate.embeddable;

import com.habit.thehabit.club.command.domain.aggregate.Club;
import com.habit.thehabit.member.command.domain.aggregate.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_SPACE")
@TableGenerator(
        name = "SEQ_SPACE_ID",
        table = "TBL_SEQ_SPACE",
        pkColumnValue = "SPACE_SEQ",
        allocationSize = 1
)
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "SEQ_SPACE_ID")
    @Column(name = "SPACE_ID")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Club club;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();


}
