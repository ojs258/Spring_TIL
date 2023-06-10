package jpabook.jpashop.service;

import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// 다시는 변경되지 않을 완성된 로직에 final을 붙히고
// final로 등록된 변수만 생성자를 만들어주는 롬복 어노테이션
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        //예외 처리
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).get();
    }

    @Transactional // 트랜잭셔널 어노테이션을 통해 JPA가 변동된 내용을 DB 플러쉬 하게끔 한다.
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get(); //DB와 연동되어있는 상태인 Member를 가져와
        member.setName(name); // 이름을 수정해주고
    }
}
