package hello.core.order;

import hello.core.annotaion.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository;
    // 회원 정보들을 가져옴
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    //생성자 주입 개념이 적용되지않은 코드
    private final DiscountPolicy discountPolicy;
    //할인 정책을 가져옴

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, /*@Qualifier("mainDiscountPolicy")*/@MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // 입력받은(넘겨받은) 회원 아이디를 통해
        // 회원 정보들 중에서 특정회원의 정보를 가져옴
        int discountPrice = discountPolicy.discount(member,itemPrice);
        // 할인 정책에 회원 정보를 넣어
        // 정보를 토대로 할인 될 금액을 가져옴
        return new Order(memberId, itemName, itemPrice, discountPrice);
        // 회원 정보와 할인 정책을 통해 구한 할인 될 금액과
        // 입력받은 정보를 함께 하나의 주문이라는 객체로 만들어서 리턴해줌
    }

    //테스트용
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
