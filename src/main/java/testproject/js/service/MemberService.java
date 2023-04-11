package testproject.js.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testproject.js.domain.Member;
import testproject.js.repository.MemberRepository;

import java.util.List;
import java.util.Optional;


@Transactional
public class MemberService{

    public final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public void join(Member member){

        checkDuplication(member);
        memberRepository.save(member);

    }
    public void checkDuplication(Member member){
        memberRepository.findById(member.getId())
                .ifPresent(n -> {
                    throw new IllegalStateException("중복된 ID 입니다.");
                });

    }
    public List<Member> memberList(){

        return memberRepository.findAll();

    }

    public Optional<Member> findOne(String id){
        return memberRepository.findById(id);
    }
}
