package jpabook.jpashop.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 이 객체가 필드명과 클래스명이 같음은 객체에 내장되게 하는 기능 
@Getter
public class Address {

    private String City;

    private String street;

    private String zipcode;

    protected Address(){
    }

    public Address(String city, String street, String zipcode) {
        City = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
