package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;

//@Component
public class FixDiscountPolicy implements DiscountPolicy{

    // 고정 할인 정책
    // Grade.VIP가 참이면 무조건 할인 금액은 1000이다.
    // 라고 return해준다.
    private int discountFixAmount = 1000; // 1000원 할인
    @Override
    public int discount(Member member, int price) {
        //EnumType을 밸류로 비교할 때는 "=="로 비교한다.
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else{
            return 0;
        }
    }
}
