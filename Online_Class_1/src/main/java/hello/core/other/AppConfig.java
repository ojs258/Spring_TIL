package hello.core.other;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 생성자 주입 - 밖에서 객체가 생성되어서 들어간다.
@Configuration
// 어셈블러, 오브젝트 팩토리 역할을하는 구성정보 파일임을 명시하는 어노테이션
public class AppConfig {
    @Bean // 빈의 이름은 관례상 메소드의 이름으로 설정됨 @Bean(이름)dmfh 설정해 줄 수도 있음.
    public static MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public DiscountPolicy discountPolicy() {
        // return  new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
