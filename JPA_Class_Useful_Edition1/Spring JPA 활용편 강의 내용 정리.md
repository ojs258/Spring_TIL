# Spring JPA 활용편 강의

DB의 연관관계에서 값의 변경이 있을 때는 foreign key를 기준으로 변경이 진행됩니다. 그러나 자바의 객체에서는 테이블 도메인을 구성하고 서로 연관관계를 매핑했을 때 어디가 foreign key이고 어디가 변경됐을 때 어떻게 따를지가 모호하다.

JPA에서는 서로 연관관계를 가지고 있는 두 객체 중 하나를 주인 객체로 잡고 해당 객체의 값이 변경되었을 때 그것을 기준으로 변경을 진행해야한다. 주인 객체를 지정하는 기준은 DB 설계에서 foreign key와 가까운 테이블이 주인 테이블로 정하는 것이 좋다.

게터와 세터

값 타입은 변경 불가능하게 설계해야 한다.

@Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자. JPA 스펙상 엔티티나 임베디드 타입( @Embeddable )은 자바 기본 생성자(default constructor)를 public 또는 protected 로 설정해야 한다. public 으로 두는 것 보다는 protected 로 설정하는 것이 그나마 더 안전하다.
JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문이다

컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.

1. null 문제에서 안전하다.
2. 하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다. 따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.

관계 어노테이션의 종류와 옵션

1:1 @OneToOne
1:N @OneToMany
N:1 @ManyToOne
N:N @ManyToMany

옵션

mappedBy = “”
거울체가 되는 객체에 추가하는 옵션 주체가 되는 FK가 있는 객체(엔티티)의 어떤 필드(컬럼)과 관계가 매핑 될 것인지 지정하는 옵션
주체가 되는 객체에서는 @JoinColumn(name = "")을 통해 FK가 될 컬럼명을 지정하며 관계 지정 어노테이션으로 연결된 필드와 조인관계를 매핑한다.

cascade = CasecadeType.옵션
연관 객체(엔티티)의  필드(컬럼)가 한번에 여러개 수정되는 경우에 여러번 쿼리가 날아가는것을 한번에 날아가게 해준다.

★단일 참조 하위 객체 일때만 사용하는게 좋다.

```java
@ManyToMany(fetch = FetchType.*LAZY*)
@JoinTable(name="category_item",
						joinColumns = @JoinColumn(name="category_id"),
						inverseJoinColumns = @JoinColumn(name="item_id"))
```

N:M (다 대 다) 조인의 경우 위와 같이 조인 테이블을 만드는 어노테이션을 통해 양방향 조인을 한다.

특징

? ToOne 형태의 관계 어노테이션은 기본적으로  eagle(즉시 로딩)으로 설정되어있다. 
fetch = FetchType.*LAZY 옵션을 통해* 지연로딩으로 바꿔 주어야한다. 서버의 퍼포먼스 측면에서도 지연 로딩이 이득이고 즉시 로딩은 디비와의 연결부나 쿼리 부분에서 버그 발생시 잡아내기 힘들다.

OneTo ?  형태는 기본적으로 lazy(지연 로딩)으로 되어있다. 목표는 전부 지연로딩으로 설정하는 것이기 때문에 가만히 나둬도 된다.

객체간 연관관계 코딩

서비스 구현체에서 DB연관관계를 전부 확인하면서 서비스를 구현해야 하는 상황을 방지하기위해 도메인 구현체에서 서로의 연관관계를 쉽게 선언할 수 있게 메서드를 만들어준다.

부모 자식 관계를 가지는 객체(엔티티) dtype 하위 객체

@Inheritance(*SINGLE_TABLE*)

@DiscriminatorValue("A")

위 세개의 어노테이션은 하나의 객체가 여러 종류의 객체를 담을 수 있는 상속 구조를 가질 때 사용한다. ex) Tiket - (Movie, Musical, Musium..)

@Inheritance
부모 객체에서 자식 객체들을 어떻게 테이블을 생성해서 조인할 것인지 전략을 설정하는 어노테이션이다.

(strategy = InheritanceType.옵션)

JOINED : 하위 객체도 전부 테이블로 생성해 기본키 + 왜래키로 서로 매핑하는 옵션SINGLE_TABLE : 부모의 테이블에 자식역할을 하는 컬럼을 전부 넣어주는 방식
TABLE_PER_CLASS : 각각의 자식테이블을 만들고 거기게 부모 역할의 컬럼을 전부 넣어주는 방식

@DiscriminatorColumn
자식 테이블을 조회할 때 구분자로 사용할 컬럼을 만들어주는 어노테이션이다. 기본으로는 DTYPE으로 설정되어있다. @DiscriminatorValue 어노테이션을 통해 타입의 이름을 지정해줄 수 있다.  Movie객체에서 @DiscriminatorValue(”M”)으로 지정하고 Movie테이블의 정보를 가져올때 where절에 해당 정보의 타입이 “M”이 맞는지 확인하고 가져온다.

@Embedded 주체가 되는 객체(엔티티)에 적는  어노테이션으로 해당 필드(컬럼)가 가지는 컬럼들이 엔티티의 내장될것을 암시하는 어노테이션이다.
@Embeddable
내장체가 되는 객체에 적는 어노테이션으로 해당 객체의 필드들이 @Embedded 어노테이션이 있는 객체(엔티티)에게 내장된다는 것을 암시하는 어노테이션이다. 
두 어노테이션을 통해 만들어진 테이블을 보면 두 객체의 필드를 하나의 객체에 전부 선언한 것처럼 만들어지지만 실제로는 객체를 두개 사용했다.

(Member → Address), (Delivery → Address)
이 처럼 여러개의 똑같은 컬럼이 다른 두개의 메인 엔티티를 구성하게 설계되었을때 사용한다.
(멤버에도 주소가 필요하고 배달에도 주소가 필요함)

EntityManager는 @PersistenceContext 이 어노테이션으로 등록해서 사용해야한다.
하지만, 스프링 부트와 JPA를 같이 사용할 때 상호 지원하는 어노테이션으로@Autowired가 존재해서 IOC 작업을 해주는 것 과 똑같이 할 수 있다.
EntityManager는 로직 실행도중 바뀔일이 절대 없으므로
final를 붙히고 클래스 단에 @RequiredArgsConstructor을 붙혀서
IOC 작업을 해주는 것 과 똑같이 @PersistenceContext를 할 수 있다.

jpql 쿼리문을 직접 작성할 때 기본적인 양식

```java
public List<Order> findAll(OrderSearch orderSearch) {

        return em.createQuery("select o from Order o join o.member m" +
                        "where o.status = :status" +
                        "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }
```

jqpl+동적쿼리(Querydsl) 공부하기 

도메인 모델 패턴 = 객체에 대한 비즈니즈 로직은 도메인 객체에 구현

트랜잭션 스크립트 패턴 = 비즈니스 로직은 서비스 구현체에 전부 구현

dirtyChecking = 변경내역감지 ★
레파지토리 객체에 업데이트 비즈니스 로직을 구현해서 파라미터로 넘어온 form 객체의 내용을 영속성이 유지되고있는 객체에 set해주고 @Trasactional 어노테이션으로 JPA의 flush를 유도해 결과적으로 변경내역감지를 구현한다.

```java
@Transactional
    public void updateItem(Long itemId, Book bookparam) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(bookparam.getPrice());
        findItem.setName(bookparam.getName());
    } // 기본적인 틀이다.
```

이것 조차도 DTO로 묶어서 set해주거나 메소드를 하나 만들어서 구현하는게 더 좋다.

★ Setter의 사용을 줄여야 좋다.

merge = 병합

hirenate가 파라미터로 넘어온 객체의 정보를 영속성을 가지는 임시객체를 하나 만들어서 거기에 전부 밀어넣고 그걸 flush해버린다. 정말 간단한 로직을 구현할 때는 간편하게 사용 할 수 있어서 좋지만 전부 밀어넣어 버린다는 부분이 변하지않아야하거나 다른 로직에 의해 변하는 중인 값조차 밀어넣어서 영속 시킬 수 있기 때문에 지양해야한다.

Assertions.assertThrows(RuntimeException.class, () -> {

DoSomething.func();

});

@NotEmpty + @Vaild

@RequestParam

@ModelAttribute(”파라미터 명”) 객체타입 객체명

뷰에서 파라미터로 받아온 객체를 바로 다시 모델객체에 어트리뷰트해주는 어노테이션

@PathVariable(”파라미터 명”) 변수타입 변수명

뷰에서 파라미터로 받아온 특정 값을 변수로 저장해 주는 어노테이션