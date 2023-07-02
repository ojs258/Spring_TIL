package study.QueryDslClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.QueryDslClass.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {

    // select * from member m where m.username = :username
    List<Member> findByUsername(String username);
}
