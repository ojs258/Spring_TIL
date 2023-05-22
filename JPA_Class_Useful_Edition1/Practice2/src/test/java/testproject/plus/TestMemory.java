package testproject.plus;

import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testproject.plus.domain.Member;
import testproject.plus.repository.MemberRepository;
import testproject.plus.repository.MemoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMemory{

    MemberRepository memberRepository = new MemoryRepository();

    @AfterEach
    public void AfterEach(){
        memberRepository.storeClear();
    }

    @Test
    public void save() {
        //given
        Member member = new Member("Spring", "jinseok", "1234");
        //when
        Member result = memberRepository.save(member);
        //then
        Assertions.assertThat(member).isEqualTo(result);
    }

    @Test
    public void findById() {
        //given
        Member member = new Member("Spring", "jinseok", "1234");
        memberRepository.save(member);

        //when
        Member result = memberRepository.findById(member.getId()).get();

        //then
        Assertions.assertThat(member).isEqualTo(result);
    }

    @Test
    public void delete() {
        //given
        Member member1 = new Member("Spring1", "jinseok", "1234");
        Member member2 = new Member("Spring2", "jungmin", "5678");
        Member member3 = new Member("Spring3", "myungjin", "1011");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);


        //when
        List<Member> result = memberRepository.delete(member2.getId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findAll() {
        //given
        Member member1 = new Member("Spring1", "jinseok", "1234");
        Member member2 = new Member("Spring2", "jungmin", "5678");
        Member member3 = new Member("Spring3", "myungjin", "1011");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        
        //when
        List<Member> result = memberRepository.findAll();

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
        
    }
}
