package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository{

    // @Autowired // 생성자가 하나밖에 없으면 생략가능
    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        Map<String, Object> parameters = new HashMap();

        jdbcInsert.withTableName("member")
                .usingGeneratedKeyColumns("id");

        parameters.put("name", member.getName());
        Number key = jdbcInsert
                    .executeAndReturnKey(
                        new MapSqlParameterSource(parameters));

        member.setId(key.longValue());

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?",
                memberRowMapper(), id);
        // sql 쿼리문에 RowMapper와 서치 값인 id를 넘겨줘서 쿼리문을 돌린 결과를
        // result에 저장한다.
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?",
                memberRowMapper(), name);
        // sql 쿼리문에 RowMapper와 서치 값인 name을 넘겨줘서 쿼리문을 돌린 결과를
        // result에 저장한다.
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("selec * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));

            return member;
        };
    }
}
