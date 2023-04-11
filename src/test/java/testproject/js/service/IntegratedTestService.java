package testproject.js.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import testproject.js.domain.Member;
import testproject.js.repository.MemberRepository;
import testproject.js.repository.MemoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class IntegratedTestService {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;


    @Test
    public void 회원_가입(){
        Member member = new Member();
        member.setName("진석");
        member.setId("Spring100");
        member.setPw("1018");

        memberService.join(member);

        Member result = memberService.findOne(member.getId()).get();
        assertThat(result.getId()).isEqualTo(member.getId());

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
