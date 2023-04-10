package testproject.js.repository;

import testproject.js.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
}
