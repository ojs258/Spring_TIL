package study.QueryDslClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.QueryDslClass.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom{

    // select * from member m where m.username = :username
    List<Member> findByUsername(String username);
}
