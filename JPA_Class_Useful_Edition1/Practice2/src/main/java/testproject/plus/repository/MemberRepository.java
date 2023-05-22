package testproject.plus.repository;

import testproject.plus.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(String id);
    List<Member> delete(String id);
    List<Member> findAll();
    void storeClear();
}
