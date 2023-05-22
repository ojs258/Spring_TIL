package testproject.plus.repository.Service;

import testproject.plus.domain.LoginMember;
import testproject.plus.domain.Member;
import testproject.plus.repository.MemberRepository;
import testproject.plus.repository.MemoryRepository;

import java.util.List;
import java.util.Optional;

public class ServiceRepository {

    MemberRepository memberRepository = new MemoryRepository();

    public Optional<Member> joinMember(Member member){

        duplicationCheck(member);
        Member joinMem = memberRepository.save(member);
        return Optional.of(joinMem);
    }

    public void duplicationCheck(Member member){
        memberRepository.findById(member.getId())
                .ifPresent(n -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> delMember(String id){
        return memberRepository.delete(id);
    }
    public List<Member> listMember(){
        return memberRepository.findAll();
    }
    public Optional<Member> loginMember(LoginMember lMember) {
        return memberRepository.findById(lMember.getId());
    }
}
