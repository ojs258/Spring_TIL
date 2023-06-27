package study.QueryDslClass;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.QueryDslClass.entity.Member;
import study.QueryDslClass.entity.QMember;
import study.QueryDslClass.entity.Team;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static study.QueryDslClass.entity.QMember.member;
import static study.QueryDslClass.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory query;



    @BeforeEach
    public void before() {
        query = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
    }

    @Test
    public void startJPQL(){
        Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(result.getUsername()).isEqualTo("member1");
    }
    
    @Test
    public void startQuerydsl() throws Exception {
        Member result = query
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(result.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() throws Exception {
        Member result = query.selectFrom(member)
                .where(member.username.eq("member1"),
                        member.age.eq(10))
                .fetchOne();

        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getAge()).isEqualTo(10);
    }

    @Test
    public void resultFetch() throws Exception {
        List<Member> fetch = query.selectFrom(member)
                .fetch();// 결과가 여러 건

        Member fetchOne = query.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();// 결과가 단 건

        Member first = query.selectFrom(member)
                .fetchFirst(); // 첫 건

        QueryResults<Member> result = query.selectFrom(member)
                .fetchResults(); // JPA의 페이징 처럼 count쿼리와 select쿼리를 둘 다 날려줌

        long total = result.getTotal();
        List<Member> results = result.getResults();
        System.out.println("results = " + results);
        System.out.println("total = " + total);

        long count = query.selectFrom(member)
                .fetchCount(); // select쿼리를 다 지우고 count 쿼리로 바꿔서 날려줌

        System.out.println("count = " + count);
    }
    
    @Test
    public void sort() throws Exception {
        //given
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        //when
        List<Member> result = query.selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("member5");
        assertThat(result.get(1).getUsername()).isEqualTo("member6");
        assertThat(result.get(2).getUsername()).isNull();
    }

    @Test
    public void paging1() throws Exception {
        List<Member> result = query.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
        assertThat(result.size()).isEqualTo(2);
    }
    @Test
    public void paging2() throws Exception {
        QueryResults<Member> result = query.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);
    }
    
    @Test
    public void aggregation() throws Exception {
        Tuple result = query.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetchOne();
        assertThat(result.get(member.count())).isEqualTo(4);
        assertThat(result.get(member.age.sum())).isEqualTo(100);
        assertThat(result.get(member.age.avg())).isEqualTo(25);
        assertThat(result.get(member.age.max())).isEqualTo(40);
        assertThat(result.get(member.age.min())).isEqualTo(10);

    }
    
    // 그룹 바이를 통한 팀의 이름과 각팀의 평균 연령
    @Test
    public void group() throws Exception {
        List<Tuple> result = query.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    // teamA에 소속된 모든 회원.
    @Test
    public void join() throws Exception {
        List<Member> result = query.selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("username")
                .containsExactly("member1", "member2");
    }
    // 회원의 이름이 팀이름과 같은 회원을 조회
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = query
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result).extracting("username")
                .containsExactly("teamA", "teamB");
    }

    // 회원과 팀을 조인하면서, 팀이름이 teamA인 팀만 조인 회원은 모두 조회
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = query.select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    // 연관관계 없는 외부조인
    // 회원의 이름이 팀이름과 같은 대상을 외부 조인
    @Test
    @Rollback(value = false)
    public void join_on_noRelationship() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = query
                .select(member,team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void noFetchJoin() throws Exception {
        em.flush();
        em.clear();

        Member result = query.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(result.getTeam());
        assertThat(loaded).isFalse();
    }

    @Test
    public void useFetchJoin() throws Exception {
        em.flush();
        em.clear();


        Member result1 = query.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        Member result2 = query.selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(result2.getTeam());
        assertThat(loaded).isTrue();

        System.out.println("result1 = " + result1);
        System.out.println("result1 = " + result2);
    }

    @Test
    public void teamTest() throws Exception {
        List<Team> result = query.selectFrom(team)
                .fetch();
        for (Team team1 : result) {
            System.out.println(team1);
        }
    }

    // 나이가 가장 많은 회원을 조회
    QMember memberSub = new QMember("memberSub");
    @Test
    public void subQuery() throws Exception {
        List<Member> result = query.selectFrom(member)
                .where(member.age.eq(select(memberSub.age.max())
                        .from(memberSub)

                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    public void subQueryGoe() throws Exception {
        List<Member> result = query.selectFrom(member)
                .where(member.age.goe(select(memberSub.age.avg())
                        .from(memberSub)

                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    public void subQueryIn() throws Exception {
        List<Member> result = query.selectFrom(member)
                .where(member.age.in(select(memberSub.age)
                        .from(memberSub)
                        .where(memberSub.age.gt(10))
                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    public void selectSubQuery() throws Exception {
        List<Tuple> result = query.select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void basicCase() throws Exception {
        List<String> result = query.select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void complexCase() throws Exception {
        List<String> result = query.select(new CaseBuilder()
                        .when(member.age.between(0, 25)).then("학생")
                        .when(member.age.between(26, 40)).then("성인")
                        .otherwise("노인"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("member = " + s);
        }
    }
    
    @Test
    public void constant() throws Exception {
        List<Tuple> result = query.select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void concat() throws Exception {
        String result = query.select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(result).isEqualTo("member1_10");
    }
}
