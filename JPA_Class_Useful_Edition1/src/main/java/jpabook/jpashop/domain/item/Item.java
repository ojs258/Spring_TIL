package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.order.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 아이템의 하위에있는 Album, Book, Movie 이 세가지 하위 테이블을 조인하는 전략을 설정하는 어노테이션
// JOINED 하위 객체도 전부 테이블로 생성해 기본키 + 왜래키로 서로 매핑하는 옵션
// SINGLE_TABLE 하나의 테이블에 전부 때려넣어 매핑없이 사용하는 옵션
// TABLE_PER_CLASS 자식들의 테이블을 만들고 각각의 테이블에 부모기능을하는 컬럼들을 넣어서
// 매핑없이 사용하는 옵션 (비추천) 누가 봐도 별로임 ㅋㅋ 근데 또 필요한 경우가 있겠지
@DiscriminatorColumn(name="dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
