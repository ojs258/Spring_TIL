package testproject.plus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testproject.plus.domain.Member;
import testproject.plus.repository.MemberRepository;
import testproject.plus.repository.MemoryRepository;

import java.util.List;
import java.util.Optional;

public class TestMemory{

    MemberRepository memberRepository = new MemoryRepository();

    @Test
    public void save() {
        //given
        Member member = new Member("Spring", "jinseok", "1234");
        //when
        Member result = memberRepository.save(member);
        //then
        Assertions.assertThat(result.getId()).isEqualTo(member.getId());
    }

    @Test
    public void findById() {
        //given
        Member member = new Member("Spring", "jinseok", "1234");
        //when
        //then
    }

    @Test
    public void delete() {
        //given
        Member member = new Member("Spring", "jinseok", "1234");
        String id = new String();
        //when
        //then
    }

    @Test
    public void findAll() {
        //given
        //when
        //then
    }
}
