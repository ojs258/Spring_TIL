package testproject.plus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testproject.plus.domain.LoginMember;
import testproject.plus.domain.Member;
import testproject.plus.repository.MemberRepository;
import testproject.plus.repository.MemoryRepository;
import testproject.plus.repository.Service.ServiceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestService {

    ServiceRepository serviceRepository = new ServiceRepository();

    MemberRepository memberRepository = new MemoryRepository();

    @AfterEach
    public void afterEach(){
        memberRepository.storeClear();
    }

    @Test
    public void 회원가입(){
        //given
        Member member = new Member("JS", "오진석", "1234");

        //when
        Optional<Member> result = serviceRepository.joinMember(member);

        //then
        Assertions.assertThat(result.get()).isEqualTo(member);
    }

    @Test
    public void 아이디_중복_확인(){
        //given
        Member member1 = new Member("JS", "오진석", "1234");
        Member member2 = new Member("JS", "이진석", "4564");

        //when
        serviceRepository.joinMember(member1);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
        () -> serviceRepository.joinMember(member2));
    }
    @Test
    public void 회원삭제(){
        //given
        Member member1 = new Member("JS", "오진석", "1234");
        Member member2 = new Member("HS", "오현석", "5678");
        Member member3 = new Member("SM", "오세만", "1011");
        serviceRepository.joinMember(member1);
        serviceRepository.joinMember(member2);
        serviceRepository.joinMember(member3);

        //when
        List<Member> result = serviceRepository.delMember(member1.getId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
    @Test
    public void 회원목록(){
        //given
        Member member1 = new Member("JS", "오진석", "1234");
        Member member2 = new Member("HS", "오현석", "5678");
        Member member3 = new Member("SM", "오세만", "1011");
        serviceRepository.joinMember(member1);
        serviceRepository.joinMember(member2);
        serviceRepository.joinMember(member3);

        //when
        List<Member> result = serviceRepository.listMember();

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }
    @Test
    public void 로그인(){
        //given
        Member member = new Member("JS", "오진석", "1234");
        serviceRepository.joinMember(member);
        LoginMember lMember = new LoginMember("JS","1234");
        Optional<Member> result = serviceRepository.loginMember(lMember);

        Assertions.assertThat(result.get().getId()).isEqualTo(lMember.getId());
        Assertions.assertThat(result.get().getPw()).isEqualTo(lMember.getPw());
        System.out.println(result.get().getId()+result.get().getPw()+result.get().getName());

    }
}
