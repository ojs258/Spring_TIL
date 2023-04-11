package testproject.js.repository;

import org.springframework.stereotype.Repository;
import testproject.js.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryRepository implements MemberRepository {

    private static HashMap<String, Member> temp = new HashMap<>();

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
    public void clearTemp(){
        temp.clear();
    }
}
