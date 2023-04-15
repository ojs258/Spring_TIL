package testproject.js.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testproject.js.domain.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, String>, MemberRepository {

    @Override
    Optional<Member> findById(String id);
}
