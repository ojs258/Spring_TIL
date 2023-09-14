package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MemberJPARepositoryTest {

    @Autowired
    MemberJPARepository memberJPARepository;

    @Test
    public void testMember() {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberJPARepository.save(member);

        //when
        Member findMember = memberJPARepository.find(savedMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJPARepository.save(member1);
        memberJPARepository.save(member2);

        Member findMember1 = memberJPARepository.findById(member1.getId()).get();
        Member findMember2 = memberJPARepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJPARepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count1 = memberJPARepository.count();
        assertThat(count1).isEqualTo(2);

        memberJPARepository.delete(member1);
        memberJPARepository.delete(member2);
        long count2 = memberJPARepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    public void findByUsername() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberJPARepository.save(memberA);
        memberJPARepository.save(memberB);

        List<Member> result = memberJPARepository.findByUsername("BBB");

        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findByPage() {
        memberJPARepository.save(new Member("memberA", 10));
        memberJPARepository.save(new Member("memberB", 10));
        memberJPARepository.save(new Member("memberC", 10));
        memberJPARepository.save(new Member("memberD", 10));
        memberJPARepository.save(new Member("memberE", 10));

        List<Member> members = memberJPARepository.findByPage(10, 0, 3);
        long count = memberJPARepository.totalCount(10);

        assertThat(members.size()).isEqualTo(3);
        assertThat(count).isEqualTo(5);
    }

    @Test
    public void bulkAgePlus() {
        //given
        memberJPARepository.save(new Member("memberA", 10));
        memberJPARepository.save(new Member("memberB", 19));
        memberJPARepository.save(new Member("memberC", 20));
        memberJPARepository.save(new Member("memberD", 21));
        memberJPARepository.save(new Member("memberE", 40));

        //when
        int resultCount = memberJPARepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

}