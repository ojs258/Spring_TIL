package hello.core.singleton;

public class
StatefulService {

//    private int price; // 상태를 유지하는 필드
    // 사용자 A의 주문인 10000이 B의 주문 20000원으로 덮어 씌워져서
    // 나중에 출력했을때 A의 주문도 B의 주문도 20000원으로 출력됨

//    public void order(String name, int price) {
//        System.out.println("name = " + name + "price = " + price);
//        this.price = price; // 여기가 문제
//    }

    public int order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
//        this.price = price; // 여기가 문제
        return price;
    }

//    public int getPrice() {
//        return price;
//    }
}