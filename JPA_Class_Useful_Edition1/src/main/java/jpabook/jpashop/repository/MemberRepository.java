package jpabook.jpashop.repository;

import jpabook.jpashop.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // select * from Member where m.name = (:name)
    List<Member> findByName(String name);

    // select * from User where u.username = (:name)
    // List<User> findByUsername(String name);

}
