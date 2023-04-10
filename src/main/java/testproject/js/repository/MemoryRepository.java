package testproject.js.repository;

import testproject.js.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MemoryRepository implements MemberRepository {

    HashMap<String, Member> temp = new HashMap<>();

    @Override
    public Member save(Member member) {
        temp.put(member.getId(),member);
        return null;
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(temp.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(temp.values());
    }
    public void clear(){
        temp.clear();
    }
}
