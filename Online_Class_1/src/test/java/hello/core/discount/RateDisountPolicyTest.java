package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RateDisountPolicyTest {
    DiscountPolicy disountPolicy = new RateDiscountPolicy();
    @Test
    @DisplayName("VIP는 10% 할인되어야한다.")
    void vip_o(){
        //given
        Member member = new Member(1L,"memberA", Grade.VIP);

        //when
        int discount = disountPolicy.discount(member, 10000);

        //then
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인되지 않아야한다.")
    void vip_x(){
        //given
        Member member = new Member(2L,"memberB", Grade.BASIC);

        //when
        int discount = disountPolicy.discount(member, 10000);

        //then
        assertThat(discount).isEqualTo(0);
    }

}