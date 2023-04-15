package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    // 회원 정보들을 가져옴
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // 할인 정책을 가져옴
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
}
