package hello.core.member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository{

    public static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {

    }

    @Override
    public Member finById(Long memberId) {
        return null;
    }
}
