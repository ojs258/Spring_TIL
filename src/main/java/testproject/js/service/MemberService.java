package testproject.js.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String join(Member member){

        checkDuplication(member.getId());

        return null;
    }
    public void checkDuplication(String id){

    }
}
