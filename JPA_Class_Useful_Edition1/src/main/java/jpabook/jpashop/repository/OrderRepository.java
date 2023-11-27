package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.member.QMember;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.domain.order.QOrder;
import jpabook.jpashop.repository.order.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    public void save(Order order){
        em.persist(order);
    }
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }
    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory qf = new JPAQueryFactory(em);
        return qf.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch, member))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(OrderSearch orderSearch, QMember member) {
        if (StringUtils.hasText(orderSearch.getMemberName())){
            return null;
        }
        return member.name.like(orderSearch.getMemberName());
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return QOrder.order.status.eq(orderStatus);
    }
    public List<Order> findAllWithMD() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m"+
                        " join fetch o.delivery d", Order.class
                )
                .setFirstResult(0)
                .setMaxResults(100)
                .getResultList();
    }

//    public List<OrderItem> fullFindAll() {
//        return em.createQuery("select oi from OrderItem oi ", OrderItem.class)
//                .getResultList();
//    }
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {

        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }

}
