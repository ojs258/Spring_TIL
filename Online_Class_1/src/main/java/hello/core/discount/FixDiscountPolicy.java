package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{

    // 고정 할인 정책
    // Grade.VIP가 참이면 무조건 할인 금액은 1000이다.
    // 라고 return해준다.
    private int discountFixAmount = 1000;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
        {
            return discountFixAmount;
        } else{
            return 0;
        }
    }
}
