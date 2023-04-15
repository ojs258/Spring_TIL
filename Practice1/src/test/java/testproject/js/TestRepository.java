package testproject.js;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testproject.js.domain.Member;
import testproject.js.repository.MemoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TestRepository{

    MemoryRepository repository = new MemoryRepository();

    @AfterEach
    public void afterEach() {
        repository.clearTemp();
    }
    @Test
    public void save_findById() {
        Member member = new Member();
        member.setId("spring");
        member.setName("진석");
        member.setPw("1018");

        repository.save(member); // 임시 DB에 값 insert

        Member result2 = repository.findById(member.getId()).get();
        assertThat(result2).isEqualTo(member);
    }
    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setId("spring1");
        member1.setName("진석");
        member1.setPw("1234");
        
        Member member2 = new Member();
        member2.setId("spring2");
        member2.setName("은지");
        member2.setPw("5678");
        
        Member member3 = new Member();
        member3.setId("spring3");
        member3.setName("연주");
        member3.setPw("1011");

        repository.save(member1);
        repository.save(member2);
        repository.save(member3);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(3);
    }
}
