package study.QueryDslClass.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.QueryDslClass.dto.MemberSearchCondition;
import study.QueryDslClass.entity.Member;
import study.QueryDslClass.repository.support.CustomQuerydslRepositorySupport;

import java.util.List;

import static study.QueryDslClass.entity.QMember.member;
import static study.QueryDslClass.entity.QTeam.team;

@Repository
public class CustomSupportRepository extends CustomQuerydslRepositorySupport {

    public CustomSupportRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }
    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }
    public Page<Member> searchPageByApplyPagination(MemberSearchCondition condition, Pageable pageable) {

        return applyPaginationAndCounting(pageable,
                contentQuery -> selectFrom(member)
                    .leftJoin(member.team,team)
                    .where(usernameEq(condition.getUsername()),
                            teamNameEq(condition.getTeamName()),
                            ageGoe(condition.getAgeGoe()),
                            ageLoe(condition.getAgeLoe())),
                countQuery -> select(member.id.count())
                    .from(member)
                    .where(usernameEq(condition.getUsername()),
                            teamNameEq(condition.getTeamName()),
                            ageGoe(condition.getAgeGoe()),
                            ageLoe(condition.getAgeLoe())));
    }

    private BooleanExpression usernameEq(String username) {
        return username.isEmpty()? null : member.username.eq(username);
    }
    private BooleanExpression teamNameEq(String teamName) {
        return teamName.isEmpty() ? null : team.name.eq(teamName);
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }
}
