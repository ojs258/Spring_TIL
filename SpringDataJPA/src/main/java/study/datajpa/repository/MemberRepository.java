package study.datajpa.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션 리턴

    Member findMemberByUsername(String username); // 단건 리턴

    Optional<Member> findOptionalByUsername(String username); // Optional 단건 리턴


    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    //.executeUpdate(); 와 같은 형식으로 출력되게 해주는 어노테이션 이 어노테이션이 없으면
    // SDJ는 select 쿼리의 반환 형태인 .getResultList(), .getSingleResult()로 기본 설정되어있어 에러가난다.
    // 업데이트 후 영속성 컨텍스트의 임시데이터 계층을 클리어해주는 옵션 clearAutomatically = true
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Override
    List<Member> findMemberCustom();

    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);
}
