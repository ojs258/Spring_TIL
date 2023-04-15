package testproject.plus.repository;

import testproject.plus.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MemoryRepository implements MemberRepository{

    private final HashMap<String, Member> store = new HashMap<>();

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return store.get(member.getId());
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> delete(String id) {
        store.remove(id);
        return findAll();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<Member>(store.values());
    }

    @Override
    public void storeClear(){
        store.clear();
    }
}
