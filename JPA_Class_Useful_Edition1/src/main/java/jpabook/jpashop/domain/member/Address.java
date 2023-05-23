package jpabook.jpashop.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
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
