package com.example.springpractice.Service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    // DI(Dependency Injection)의존성 주입 상위 객체가 하위 객체에서
    // 값을 올려주길 기다리는 형태?
    // 값을 받아서 사용하기때문에 때마다 new로 새로만드는것보다 코드가 정확함
    //@Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    //회원가입
    public Long join(Member member){
        /* 회원가입 서비스 구현체
         중복된 이름은 가입 X
        Optional<Member> result = memberRepository.findByName(member.getName());
         * 페이지에서 입력받은 회원객체의 이름을 findByName으로 검색해서
         * 같은 이름이 존재하면 해당 객체를 리턴(findAny())
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
         * result에 리턴 된 값이 있으면 예외처리 문구 출력
         * .ifPresent() => Optional에 참조되는 메소드*/
        /*long start = System.currentTimeMillis();

        try {*/
            //최적화된 코드
            vaildateDuplicatedMember(member);
            memberRepository.save(member);
            return member.getId();
       /* } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs);

        }*/

        // 리팩토링으로 메소드를 추출해서 중복검사 메소드를 호출하는 방식으로 구현
    }

    //추출된 중복 검사 메소드
    private void vaildateDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    //회원 전체를 출력해주는 서비스 구현체
    public List<Member> findMembers(){
        /*long start = System.currentTimeMillis();

        try {*/
            return memberRepository.findAll();
       /* } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers = " + timeMs);
        }*/

    }

    //회원 id를 조회해 id에 해당하는 한명을 출력해주는 서비스 구현체
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
