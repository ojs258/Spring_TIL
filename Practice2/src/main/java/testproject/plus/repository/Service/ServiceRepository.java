package testproject.plus.repository.Service;

import org.springframework.boot.web.server.Cookie;
import testproject.plus.domain.LoginMember;
import testproject.plus.domain.Member;
import testproject.plus.repository.MemberRepository;
import testproject.plus.repository.MemoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ServiceRepository {

    MemberRepository memberRepository = new MemoryRepository();

    public Member joinMember(Member member){

        duplicationCheck(member);
        Member joinMem = memberRepository.save(member);
        return joinMem;
    }

    public void duplicationCheck(Member member){
        memberRepository.findById(member.getId())
                .ifPresent(n -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public String delMember(String id){
        memberRepository.delete(id);
        return "삭제되었습니다.";
    }
    public List<Member> listMember(){
        return memberRepository.findAll();
    }
    public HashMap<String, String> loginMember(LoginMember lMember) {
        HashMap<String, String> resultMember = new HashMap<>();
        Optional<Member> member = memberRepository.findById(lMember.getId());
        if (lMember.getPw().equals(member.get().getPw())){
            resultMember.put(member.get().getId(),"로그인 성공");
        }else {
            resultMember.put(member.get().getId(), "로그인 실패");
        }
        return resultMember;
    }
}
