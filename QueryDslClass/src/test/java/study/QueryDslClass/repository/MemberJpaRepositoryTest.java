package study.QueryDslClass.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.QueryDslClass.dto.MemberSearchCondition;
import study.QueryDslClass.dto.MemberTeamDto;
import study.QueryDslClass.entity.Member;
import study.QueryDslClass.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired EntityManager em;

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);

        List<Member> result3 = memberJpaRepository.findAll_Querydsl();
        assertThat(result3).containsExactly(member);

        List<Member> result4 = memberJpaRepository.findByUsername_Querydsl("member1");
        assertThat(result4).containsExactly(member);
    }
    @Test
    public void searchTest() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        //when
        MemberSearchCondition con = new MemberSearchCondition();
        con.setUsername("member1");

        List<MemberTeamDto> result = memberJpaRepository.search(con);

        //then
        assertThat(result).extracting("teamName").containsExactly("teamA");

    }
}