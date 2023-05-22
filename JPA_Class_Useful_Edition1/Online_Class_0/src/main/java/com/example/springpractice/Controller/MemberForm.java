package com.example.springpractice.Controller;


// 회원가입 페이지에서 회원의 정보를 받아오는 형식을 구성하는 객체
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
