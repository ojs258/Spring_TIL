package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count1 = memberRepository.count();
        assertThat(count1).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count2 = memberRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findByUsername() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsername("BBB");

        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findUser() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findUser(memberB.getUsername(),memberB.getAge());

        assertThat(result.get(0)).isEqualTo(memberB);

    }

    @Test
    public void findUsernameList() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> result = memberRepository.findUsernameList();

        System.out.println("result = " + result);

    }

    @Test
    public void findMemberDto() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("AAA", 10, teamA);
        Member memberB = new Member("BBB", 20, teamB);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));

        System.out.println("result = " + result);

    }

    @Test
    public void findListByUsername() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> members = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
    }

    @Test
    public void findByPage() {
        memberRepository.save(new Member("memberA", 10));
        memberRepository.save(new Member("memberB", 10));
        memberRepository.save(new Member("memberC", 10));
        memberRepository.save(new Member("memberD", 10));
        memberRepository.save(new Member("memberE", 10));

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        //Pageable 인터페이스의 구현체 PageRequest
        Slice<Member> page = memberRepository.findByAge(10, pageRequest);
//        Page<Member> page = memberRepository.findByAgeP(10, pageRequest);
//        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
//        Slice<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
//        Page, Slice에 담아진 객체 DTO로 변환하는 법



        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkAgePlus() {
        //given
        memberRepository.save(new Member("memberA", 10));
        memberRepository.save(new Member("memberB", 19));
        memberRepository.save(new Member("memberC", 20));
        memberRepository.save(new Member("memberD", 21));
        memberRepository.save(new Member("memberE", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        List<Member> members = memberRepository.findByUsername("memberE");
        Member memberE = members.get(0);

        //then
        assertThat(resultCount).isEqualTo(3);
        assertThat(memberE.getAge()).isEqualTo(41);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("AAA", 10, teamA);
        Member memberB = new Member("BBB", 20, teamB);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        em.flush();
        em.clear();

        //when
        List<Member> all = memberRepository.findAll();

        for (Member member : all) {
            System.out.println("member = " + member.getUsername());
            System.out.println("Team = " + member.getTeam());
        }
        //then
    }
    
    @Test
    public void queryHint(){
        //given
        Member memberA = memberRepository.save(new Member("memberA", 10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername(memberA.getUsername());
        member.setUsername("member2");

        em.flush();

        //then
    }

    @Test
    public void callCustom() {
        List<Member> members = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        Member member = new Member("m1");
        member.setTeam(teamA);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
        assertThat(result.size()).isEqualTo(1);
    }
    
    @Test
    public void projections() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);

        //then
        for (NestedClosedProjections nestedClosedProjections : result) {
            String username = nestedClosedProjections.getUsername();
            String teamName = nestedClosedProjections.getTeam().getName();
            System.out.println("username = " + username);
            System.out.println("teamName = " + teamName);
        }
    }
    
    @Test
    public void nativeTest() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 10, teamA);
        Member member2 = new Member("m2", 20, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();

        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjectionName = " + memberProjection.getUsername());
            System.out.println("memberProjectionTeamName = " + memberProjection.getTeamName());
        }

    }
}