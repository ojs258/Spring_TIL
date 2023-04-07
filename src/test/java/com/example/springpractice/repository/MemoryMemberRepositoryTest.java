package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트 구현체 메소드가 실행될 때 마다 그 후에
    @AfterEach
    public void afterEach(){
        repository.cleatStore();
        // store.clear()를 호출해 repository값을 clear해준다.
    }
    @Test
    public void save(){
        Member member = new Member();
        member.setName("Spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);

        // 위에서 구현해준 Spring이라는 이름을 가진 객체의 id를 찾아서
        // 그 id의 객체가 위에 선언한 객체가 맞는지 확인
        // 지금은 간단한 기능이어서 2+2+2가 2*3이 맞는지 확인하는 꼴

    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
        //result에 리턴된 member객체가 member1과 일치하는지 확인
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member member3 = new Member();
        member3.setName("spring3");
        repository.save(member3);

        List<Member> result = repository.findAll();
        // 위의 생성한 초소형 DB의 의 내용물들을 ArrayList형태로 반환받아서
        // List<Member>형의 result에 저장

        assertThat(result.size()).isEqualTo(3);
        //result의 size는 곧 초소형 DB의 Member객체의 개수
        // 위에서 3개를 만들어서 넣어 주었으니까 3과 비교해서
        // 맞으면 True -> Success
    }
}
