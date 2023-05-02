package hello.core.beanFind;

import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ap = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = ap.getBean("memberService", MemberService.class);
//        System.out.println("memberService = " + memberService);
//        System.out.println("memberService.getClass() = " + memberService.getClass());
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
    @Test
    @DisplayName("구현체 타입으로 조회")
    void findBeanByName2(){
        MemberServiceImpl memberService = ap.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }
    @Test
    @DisplayName("이름 없이 타입으로 조회")
    void findBeanByType(){
        MemberService memberService = ap.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
    @Test
    @DisplayName("빈 이름으로 조회 X")
    void findBeanByNameX(){
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ap.getBean("xxxxxxx", MemberService.class));
    }

}
