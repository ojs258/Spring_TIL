package jpabook.jpashop.domain.member;

import jakarta.persistence.*;
import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_Id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 객체로 구성하는 연관관계에서 손님 객체로 인식하게하는 옵션
    private List<Order> orders = new ArrayList<>();
}
