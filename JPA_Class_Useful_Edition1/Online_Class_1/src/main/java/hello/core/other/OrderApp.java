package hello.core.other;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.other.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        ApplicationContext ap = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ap.getBean("memberService", MemberService.class);
        OrderService orderService = ap.getBean("orderService", OrderService.class);
        Long memberId = 1L;
        Member member = new Member(memberId, "membeA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 20000);
        System.out.println("order = " + order);
        System.out.println("order = " + order.calculatePrice());
    }

}
