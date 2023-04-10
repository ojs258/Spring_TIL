package testproject.js;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testproject.js.domain.Member;
import testproject.js.repository.MemoryRepository;
import testproject.js.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;

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
        memoryRepository.clear();
    }
    @Test
    public void 회원_가입(){
        Member member = new Member();
        member.setName("진석");
        member.setId("Spring");
        member.setPw("1018");

        String id = memberService.join(member);

        Member result = memoryRepository.findById(id).get();
        assertThat(result).isEqualTo(member);

    }

    @Test
    public void 중복_회원_검사(){
        Member member1 = new Member();
        member1.setName("진석");
        member1.setId("Spring");
        member1.setPw("1018");

        Member member2 = new Member();
        member2.setName("은지");
        member2.setId("Spring");
        member2.setPw("1018");


    }

    @Test
    public void 회원_목록(){

    }
}
