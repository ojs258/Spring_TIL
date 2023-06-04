package jpabook.jpashop.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String name;

    @Embedded // 어떤 객체가 지금 필드와 매핑하여 내장되게 하는 기능
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // 객체로 구성하는 연관관계에서 손님 객체로 인식하게하는 옵션
    private List<Order> orders = new ArrayList<>();
}
