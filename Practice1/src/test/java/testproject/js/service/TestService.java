package testproject.js.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testproject.js.domain.Member;
import testproject.js.repository.MemoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestService {

    MemberService memberService;
    MemoryRepository memoryRepository;

    @BeforeEach
    public void BeforeEach(){
        memoryRepository = new MemoryRepository();
        memberService = new MemberService(memoryRepository);
    }
    @AfterEach
    public void AfterEach(){
        memoryRepository.clearTemp();
    }
    @Test
    public void 회원_가입(){
        Member member = new Member();
        member.setName("진석");
        member.setId("Spring");
        member.setPw("1018");

        memberService.join(member);

        Member result = memoryRepository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);

    }

    @Test
    public void 아이디_중복_검사(){
        Member member1 = new Member();
        member1.setName("진석");
        member1.setId("Spring");
        member1.setPw("1234");

        Member member2 = new Member();
        member2.setName("정민");
        member2.setId("Spring");
        member2.setPw("5678");

        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("중복된 ID 입니다.");


        //중복입니다.exception 문구가 출력되는지로 확인

    }
}
