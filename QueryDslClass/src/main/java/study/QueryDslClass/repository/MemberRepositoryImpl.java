package study.QueryDslClass.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.QueryDslClass.dto.MemberSearchCondition;
import study.QueryDslClass.dto.MemberTeamDto;
import study.QueryDslClass.dto.QMemberTeamDto;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;
import static study.QueryDslClass.entity.QMember.member;
import static study.QueryDslClass.entity.QTeam.team;

@RequiredArgsConstructor
public class MemberRepositoryImpl /*extends QuerydslRepositorySupport*/ implements MemberRepositoryCustom{
// 이름은 규칙이 정해져있다.

/*
    public MemberRepositoryImpl(Class<?> domainClass) {
        super(domainClass);
    }
    아래의 *1 *2 *3 주석의 내용을 가능하게 해주는 QuerydslRepositorySupport 객체와 해당 객체에서 상속받아 구현되는 생성자
*/
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

/*  *1 아래 쿼리 코딩을 EntityManage와 QueryFactory선언 및 주입 없이 사용하게 해주는 것.
        from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .fetch();
 */

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team,team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> query = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<MemberTeamDto> content = query.getResults();
        long total = query.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {

        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
/*  *2 페이징 쿼리에서 Pageable을 적용하는 쿼리와 컨텐츠 로직을 분리해서 적용하는
        getQuerydsl().applyPagination(), getQuerydsl().applySorting도 제공한다.
        하지만 Sorting은 정상적으로 작동하지 않는다. 아예 매장당한 기능인듯?

        JPQLQuery<MemberTeamDto> jpqlQuery = from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name ));

        JPQLQuery<MemberTeamDto> contentV2= getQuerydsl().applyPagination(pageable, jpqlQuery);
*/
        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
        // PageableExecutionUtils의 기능으로 세개의 조건에 따라 Count쿼리를 최적화 해준다.
        // 1. 페이징이 없거나 첫 페이지부터 한 페이지의 사이즈보다 남은 컨텐츠가 적은 경우 -> 쿼리X, 남은 컨텐츠 개수
        // 2. 남은 컨텐츠가 있고 한 페이지의 사이즈보다 남은 컨텐츠가 적은 경우(마지막 페이지인 경우) -> 쿼리X, offset + 남은 컨텐츠 개수
        // 3. 나머지 경우 -> 쿼리 O
        // offset = pageSize * 현재 페이지 넘버 = 이미 이전 페이지에 출력했거나 스킵한 페이지에 포함된 항목 수





    }

    private BooleanExpression usernameEq(String username) {
        return isEmpty(username) ? null : member.username.eq(username);
    }
    private BooleanExpression teamNameEq(String teamName) {
        return isEmpty(teamName) ? null : team.name.eq(teamName);
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }
}
