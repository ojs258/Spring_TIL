package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.member.Address;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    private Member createMember(){
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강남", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus()); // 상품주문시 주문상태는 ORDER
        assertEquals(1, getOrder.getOrderItems().size()); // 주문한 상품개수는 정확해야함
        assertEquals(book.getPrice()*orderCount, getOrder.getTotalPrice()); // 주문가격 로직 확인
        assertEquals(8, book.getStockQuantity()); //주문 수량만큼 상품수량이 줄어야함
    }
    @Test
    public void 재고수량_초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);

        int orderCount = 12;

        //when & then
        assertThrows(NotEnoughStockException.class, () ->{
            orderService.order(member.getId(),item.getId(), orderCount);
        });
        //재고보다 주문 수량이 많이 때문에 예외가 발생해야 한다.
    }

    @Test
    void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(getOrder.getStatus(),OrderStatus.CANCEL); // 주문의 상태가 CANCLE인지 확인
        assertEquals(10,item.getStockQuantity()); // 주문이 취소됐기 때문에 상품 수량이 원래대로인지 확인
    }
    

}