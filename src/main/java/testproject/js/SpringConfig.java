package testproject.js;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import testproject.js.repository.JpaMemberRepository;
import testproject.js.repository.MemberRepository;
import testproject.js.repository.MemoryRepository;
import testproject.js.service.MemberService;

@Configuration
public class SpringConfig {

    private EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }

}