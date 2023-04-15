package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;

import java.util.*;


/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
//@Repository
public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        /* member라는 id와 name를 가지는 객체에
        id 부분을 지정 [long type 0]*/
        store.put(member.getId(), member);
        /* store라는 해시맵에 member의 id값 : member객체
         형태로 지정*/
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
        //해시맵의 key인 id를통해 회원정보를 가져오는 흐름
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                //.steam()해시맵의 values를 루프를 돌게함
                .filter(member -> member.getName().equals(name))
                /*루프를 돌면서 해시맵의 vaules의 member들의 name과
                입력받은 name이 같은지 확인하고 필터링*/
                .findAny();
                // 필터링된 값을 리턴해주는 부분
        //해시맵의 values인 Member객체를통해 회원정보를 가져오는 흐름
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
        //해시맵의 values는 객체들 전부이다 이를 ArrayList형태로 리턴해준다.
    }

    public void clearStore(){
        store.clear();
    }
}
