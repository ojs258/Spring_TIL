# Spring Data Jpa

Repository를 인터페이스까지만 만들고
인터페이스 이름 extends JpaRepository<사용할 객체 타입, PK 타입>

기본적인 공통 CRUD기능들을 JpaRepository라는 인터페이스에 구현되어있고
특정한 컬럼을 조회하는 로직을 만들때 스프링데이터JPA의 쿼리메소드 기능을 이용한다.

쿼리 메소드 기능 → 기본 기능

1. 메소드 이름으로 쿼리를 생성
    
    기본적인 조회 쿼리의 경우
    
    find…By , read…By , query…By , get…By
    
    와 같은 구조를 가진다.
    
    find와 By 사이에는 이 메소드의대한 간단한 설명이들어가도 된다.
    들어가지않은것과 성능차이가없다.
    
    By뒤에는 where의 들어갈 조건들과 관련된 내용을들 SDJ 문법으로 서술하면 된다.
    ex) findMemberByUsernameAndAgeGreaterThan(이름과 나이를 키값으로 나이가 더 크거나 같은 Member객체를 조회해온다.)
    
    위의 예시에는 find와 By사이에 Member라는 단어가 들어갔지만, 들어가지 않아도 리턴되는 객체는 Member 객체이다. MemberRepository로 구현하고있는 인터페이스이기 때문이고 위의 설명처럼 find와 By사이의 단어는 있어도되고 없어도되는 메소드를 설명하는 단어가 들어가는 것이다.
    
2. NamedQuery
    
    도메인객체에 @NamedQuery 어노테이션과 함께 쿼리문의 이름과 JPQL로 작성된 쿼리문을 미리 만들어놓고 
    
    1. JPA에서 em.createNamedQuery 메소드를 이용해 이름으로 불러와서 사용하고 파라미터나 페이징등의 설정을 추가해준다.
    2. SDJ 인터페이스에서 @Query 어노테이션을 통해 쿼리문 이름으로 해당 쿼리문을 가져와서 쓰는 기능 이때는 @Param 어노테이션으로 파라미터를 지정해준다.
    3. JpaRepository가 파라미터로 있는 도메인객체가서 네임드쿼리 서치도 진행하고 @Query어노테이션도 인식하기때문에 네임드쿼리의 이름을 도메인객체명.쿼리이름 형식으로 설정하면 @Query어노테이션 없이도 넴임들 쿼리를 알아서 찾아서 가져다 쓴다.
    4. 애플리케이션 로딩시점에 쿼리문 파싱을 통해 쿼리문 오류를 체크할 수 있는 기능을 제공한다.
3. @Query - 리파지토리 메소드에 쿼리 정의
    
    메소드 이름으로 쿼리생성같은 경우에 파라미터가 너무 많아지면 알아보기가 너무 힘들어진다. 
    
    이 문제를 해결하려고 data-jpa가 아닌 jpa레파지토리를 만들어서 createQuery메소드로 쿼리문을 작성하면 너무 복잡해진다.
    
    이 때 @Query 메소드를 이용해 인터페이스로 만들어진 data-jpa 레파지토리의 메소드에 직접 쿼리문을 짤 수 있다.  이것 또한 기본적인 CRUD의 개념에서만 사용하는 것이 좋고 API 로직을 위한 복잡한 동적 쿼리의 경우 API Query로직을 따로 만들어서 쓰는것이 좋다.(Query dsl이용)
    ex)
    
    ```java
    @Query("select m from Member m where m.username = :username and m.age = :age")
        List<Member> findUser(@Param("username") String username, @Param("age") int age);
    ```
    
    마찬가지로 파싱을 통한 쿼리문 검증이 가능하다.
    
    @Query 어노테이션에서 value를 담당할 쿼리문과 countQuery를 담당할 쿼리문을 나눠 줄 수 있다. 전부 Left Outer Join하는형식의 쿼리의 경우 굳이 조인이 이루어진 후에 카운트를 돌릴필요가없기 때문에 대용량의 데이터 처리가 필요하면 밸류 쿼리와 카운트쿼리를 나누어 주는게 좋다.
    
4. @Query, 값, DTO 조회하기
    
    JPQL짜는 방식과 동일하게 DTO객체를 쿼리문안에서 선언해서 DTO객체모양으로 조회해 올 수 있다.
    
5. 파라미터 바인딩
    1. 단일 파라미터 바인딩
        
        @Query를 통해 쿼리문을 작성할 때 파라미터를 바인딩해주는 @Param 어노테이션이 하는 기능이다. 메소드의 파라미터로 넘어온 변수를 JPQL의 파라미터로 지정해준다.
        
    2. 컬렉션 파라미터 바인딩
        
        JPQL의 in절에 여러개의 파라미터가 들어갈때 in절안에 컬렉션(리스트, 어레이)을 바인딩해주는 기능이다.
        
6. 반환 타입
    
    반환타입을 유연하게 사용가능하다.
    
    크게 단 건 리턴, 컬렉션 리턴, Optional로 감싼 단 건 리턴, DTO로 리턴 등이 있다.
    쿼리문을 실행했을때 값이없으면 자제적으로 예외처리를 진행해 NoResultException이 터지지않고 emptyCollection이나 null값을 리턴해준다. (Optional의 등장으로 크게 의미없어진 기능이긴 하다.)
    
    - 스프링은 같은 종류의 다른 익셉션을 스프링프레임워크의 예외로 리턴해준다.
7. 페이징과 정렬
    
    DB의 종류마다 조금씩 다른 페이징절을 JPA는 연동되어있는 DB에 맞게 알아서 번역해서 쿼리문을 번역해준다.
    
    페이징쿼리의 근본적인 로직
    
    한번에 10개씩 페이지 노출을 원하는 API의 경우 11개씩 끊어서 페이징하고 10개만 보여준다 그 때 11번째 값이 있으면 다음 페이지 버튼을 활성화하고 다음 페이지 버튼이 눌리면 11(1번째)번째 부터 21(11번째)번째까지 끊어서 페이징하고 10개만 노출시키고 21번째 값이 없으면 다음 페이지 버튼을 비활성화 시키는 로직 SDJ에선 Slice반환타입이 이 와 같은 역할을 한다. 또 Page반환 타입도 있는데 이 타입은 컨텐츠 페이징과 토탈 카운트를 가져와 개발자가 로직을 짜서 한 페이지에 몇 페이지씩 총 몇페이지를 출력시킬지 정하는 방식도 있다 요즘은 전자가 많은거같다 페이지 수로 나누는게 아니라 휠 리스너에 더보기 개념을 넣어서 아래로 계속 무한대로 내려지게 한다던지 더보기 버튼 하나만 만들어놓고 다음페이지 또 다음페이지 하는 방식.
    
    페이징 객체에 담긴 엔티티를 DTO로 변환하는 방법
    
    ```java
    Page<MemberDto> map = page
    .map(m -> new MemberDto(m.getId(), m.getUsername(), null));
    
    Slice<MemberDto> map = page
    .map(m -> new MemberDto(m.getId(), m.getUsername(), null));
    ```
    
8. 벌크성 수정 쿼리
    
    단 건 업데이트가 아닌 여러개를 동시에 업데이트하는 경우이다.
    
    @Query 어노테이션으로 업데이트 쿼리문을 작성하면 되고 업데이트 쿼리의 반환형태인 executeUpdate() → int 형태로 반환받기 위해 @Modifying 어노테이션도 붙혀준다. 
    
    ★ JPA의 업데이트 연산
    
    JPA는 영속성컨텍스트와 함께 임시데이터 개념을 가지고있다. 그렇기 때문에 더티 체킹과 같은 변경감지도 가능한 것이다. 업데이트 직후 API가 끝나는 경우에는 문제가 없지만, 업데이트 후 다른 로직이 붙는 경우 문제가 발생한다.
    
    먼저 업데이트 쿼리가 진행되면 JPA는 영속성 컨텍스트에있는 값을 기준으로 쿼리 작업후 플러쉬를 해준다. 하지만 아직 트랜잭션이 살아있는 상태에서 다음 로직을 진행하는 경우에 JPA는 처음 영속성을 얻을때 가져온 임시데이터를 기준으로 다음 쿼리를 돌리게된다.
    
    이 때 DB는 업데이트가 적용되었지만 영속성 컨텍스트의 임시데이터에는 업데이트가 적용되지않아서 이상한 결과를 얻게된다. 이를 방지하기 위해 업데이트 쿼리 이후 트랜잰셕 커밋없이 다음 로직을 진행하게 되면 로직 사이에 영속성 컨텍스트를 클리어해줘서 다시 기준이 되는 임시데이터를 DB에서 가져오게 해야한다.  SDJ에서는 @Modifying 어노테이션의 옵션으로 clearAutomatically 옵션으로 True로 주어 해결 할 수 있다.
    
9. @EntityGraph
    
    JPA의 페치조인을 SDJ에서 직접 JPQL 쿼리문 작성없이 할 수 있게 해주는 어노테이션
    
    @EntityGraph(attributePaths = {””})
    
    이 옵션으로 페치조인을 할 엔티티명을 넣어주면 알아서 조인쿼리문을 만들어서 보내준다. 기본적인 규칙은 당연히 지켜야한다. 컬렉션 조인의경우 동적쿼리로 짜고 toOne 관계는 이어지는 toOne관계도 계속해서 페치조인해도 된다. ….등등
    
    @NamedEntityGraph 어노테이션도 있는데 @NamedQuery 쿼리처럼 name 옵션으로 이름을 지정해주고 사용하면 된다. 단 attributePath 부분에 들어갈 엔티티들을 attributeNodes = @NamedAttributeNodes어노테이션을 통해 넣어줘야한다. 
    
    마찬가지로 쿼리가 복잡해 지면 그냥 JQPL로 짜거나 QueryDsl로 짜는게 좋다.
    
10. JPA HINT&LOCK
    1. HINT
        
        아래에 설명하는 트래픽이 많다는 개념은 적당히 많은 수준을 말한다. 정말 트래픽이 많으면 따로 성능 개선 처리를 해준다.
        
        조회 API의 트래픽이 많아져서 성능저하가 생길때 약간의 성능 개선 처리로 하이버네이트의 readonly 옵션을 줄 수있다. SDJ에서 이것을 사용하게 해주는 @QueryHints, @QueryHint 어노테이션이 있다. @QueryHints로 @QueryHint를 감싸서 사용하며 @QueryHint의 옵션으로는 name, value가 있다 name에는 힌트를 넣어주고 value에는 힌트 옵션에 대한 boolean값을 넣어준다. 대표적으로 조회 API에 대한 쿼리에서 readOnly옵션을 true로 주는데 사용한다.
        

확장 기능

1. 사용자정의 리포지토리 정의
    
    사용자 정의 리포지토리를 사용하여 SDJ나 JPA가 아닌 다른 방식의 SQL쿼리를 만들어주는 프레임워크를 이용해야 할 때 사용자 정의 방식을 이용할 수 있다.
    
    1. 임의의 인터페이스를 하나 만들고 필요한 메소드를 선언한다.
    2. 해당 인터페이스의 구현체에 다른 프레임워크를 사용한 로직 구현을 진행한다.
    3. 마지막으로 해당 인터페이스를 SDJ 인터페이스에 상속시켜준다.
    
    ```java
    public interface MemberRepository extends
    JpaRepository<Member, Long>, MemberRepositoryCustom
    ```
    
    JpaRepository<Member, Long>
    → Member객체를 다루는 SDJ 인터페이스를 상속받는것
    
    MemberRepositoryCustom
    → 임의로 만든 사용자정의 인터페이스 또한 상속받는것
    
    이 때 메인이 되는 SDJ 인터페이스와 임의로 만든 인터페이스의 구현체간의 규칙이 생긴다. SDJ가 이를 인식해서 서로 연결해 주려면 필요한 규칙이있는데 임의의 인터페이스 이름은 임의로 지정하고 구현체의 이름은 메인이 되는 SDJ인터페이스 이름 + Impl로 생성해야한다.
    
    ex)
    
    MemberRepository(인터페이스) → MemberRepositoryImpl(구현체)
    
    사이에 끼게되는 인터페이스 이름은 알아 볼 수 있게만 지으면 된다.
    특별한 규칙 X
    
    이것이 고대하던 SDJ에 QueryDsl을 석는법!!! 하지만 SDJ가 지원하는 핵심 비즈니스 로직과 흐름이 같은 임의의 로직이 추가되어야 할 때 이 기능을 추천한다.
    
    임의로 구현해야하는 복잡한 로직의 흐름이 완전히 비핵심 로직이거나  커맨드로 구현하는것이 불가능한 페이지에 딱 맞게 구현해야하는 쿼리거나 라이프 사이클이 다른 방향이거나 등의 이유에 해당하는지 종류를 잘 파악하고 따로 구현하는것을 권장한다.
    
1. Auditing
    1. JpaBaseEntity (JPA을 이용한 Auditing)
        
        @MappedSuperclass Auditing객체에 필드들을 상속받은 엔티티에서 해당 필드들을 컬럼으로 인식하게 해주는 어노테이션을 달아준다.
        
        @PrePersist, @PreUpdate, @PostPersist, @PostUpdate
        
        위의 어노테이션과 함께 메소드를 하나 구현하여 산 처리로직(생성, 수정 시간 남기기), 후 처리 로직(생성, 수정한 사용자 이름 남기기)등을 구현해준다.
        
        엔티티 객체에서 Auditing객체를 상속(extends)해준다.
        
    2. BaseEntity (SDJ를 이용한 Auditing)
        
        @EnableJpaAuditing SDJ에게 해당 프로젝트가 Auditing을 가질 수 있다고 알려주는것 main클래스에 달아준다. 
        
        @CreatedDate, @LastModifiedDate (생성, 수정 시간남기기)
        
        @CreatedBy, @LastModifiedBY (생성자, 수정자 이름 남기기)
        
        Auditing을 기능 자체가 시간을 찍거나 생성자, 수정자를 남기는 용도로 밖에 사용되지않으니까 SDJ에서는 아예 어노테이션으로 기능을 뽑아서 지원한다. 
        
        @EntityListeners(AuditingEntityListener.class) SDJ가 해당 객체를 Auditing객체로 인식할 수 있게 해준다.
        
        JPA Auditing과 같이 @MappedSuperclass Auditing객체에 필드들을 상속받은 엔티티에서 해당 필드들을 컬럼으로 인식하게 해주는 어노테이션을 달아준다.
        
        엔티티 객체에서 Auditing객체를 상속(extends) 해준다.
        

도메인 클래스 컨버터

1. 단건 조회
    
    아무리 생각해도 쓸데없는 기능인거같다. 하지만 공부를 해둬야 나중에 알아볼 수 있으니까 정리.
    
    ```java
    @GetMapping("/members/{id}")
        public String findMember(@PathVariable("id") Long id) {
            Member member = memberRepository.findById(id).get();
    
            return member.getUsername();
        }
    ```
    
    ```java
    @GetMapping("/members2/{id}")
        public String findMember2(@PathVariable("id") Member member) {
            return member.getUsername();
        }
    ```
    
    두 코드가 똑같이 동작한다. findById 없이 id가 일치하는 member객체를 가져온다.
    
    엔티티를 직접 밖에 내는 것도 맘에 안들고 위의 경우와 비슷한 복잡도의 쿼리에서만 작동하는데 의미가 없는거같다 보통의 API는 이렇게 단순한 쿼리로 작동하지 않는다.
    
    또 영속성 컨텍스트의 범위가 너무 애매해지기 때문에 정말 단순 조회용으로만 사용해야한다.
    
2. 여러건 조회
    
    SDJ의 메소드들을 Sort, Pageable을 파라미터로 넘겨서 작동하게 할 수 있다. 이를 이용해 컨트롤러를 이용해 URL로 페이징설정을 해줄 수 있다. 컨트롤러에 Pageable객체를 매개변수로 지정해주고 SDJ 여러건 조회 메소드에 파라미터로 넣어주면 아래와 같이
    
    [localhost:8080/members?page=0&size=5&sort=id,desc](http://localhost:8080/members?page=0&size=5&sort=id,desc)
    
    URL로 페이징을 할 수있다.
    
    page ⇒ 몇번째 페이지
    size ⇒ 한페이지에 몇 개 까지
    sort ⇒ 정렬 기준, 정렬 방식 asc의 경우 정렬방식은 생략해도 됨.
    
    @PageableDefault 어노테이션을 Pageable객체 매개변수 앞에 붙혀서 URL에 어떤 옵션도 주지않았을때 기본적인 출력 방법을 정할 수있다.
    
    @Qualifier 어노테이션과 함께 써서 엔티티 별로 페이징 옵션을 다르게 줄 수 있다.
    
    Page 인덱스를 1부터 시작하는 설정도 있으나 JSON데이터의 같이 넘어가는 토탈 객체의 값이나 Pageable객체의 값과 일치하지않는 문제가 발생한다. 그래서 잘 사용하지 않으나 굳이 제약을 달고 사용하겠다면 사용해도 큰 문제는 없다. 
    

SDJ 구현체

SDJ는 인터페이스만으로 거의 모든 CRUD로직을 지원한다.

해당 기능들이 구현되어있는 구현체를 열어보면

@Repository 어노테이션을 통해 레파지토리 로직으로 스프링 컨테이너가 인식하고 관리할 수 있게 한다. 그렇기 때문에 다른 쿼리 라이브러리(JDBC, JPA, 네이티브 쿼리)들을 사용해도 마찬가지로 적용되는 스프링의 예외 추상화(내 맘대로 지은 이름)기능이 동작한다.

@Transactional 어노테이션을 통해 영속성 컨텍스트를 가지면서 시작한다. 서비스 계층에서 영속성 부여없이 메소드만 호출해도 repository로직이 수행되는 중요한 이유, 서비스 계층에서 영속성을 가지고 메서드로 넘어가게되면 그 영속성을 전파받아서 사용한다.

구현체의 @Transactional에는 readOnly = true 옵션이 붙어있다. 가장 많이 호출되는 조회 로직의 경우 readOnly 동작하는 것이 트랜잭션이 커밋 될 때 마다 매번 진행되는 영속성 플러쉬를 진행하지 않기때문에 성능면에서 조금 더 유리하기 때문이다.

메서드 단에서 붙히는 어노테이션이 더 우선순위를 가지기 때문에 삽입이나, 변경이 필요한 메서드 위에 그냥 @Transactional이 따로 붙어있다.

새로운 엔티티 구별하기

영속성 컨텍스트와 캐시데이터 그리고 플러쉬 ⇒ 더티체킹, 변경감지 (압도적으로 유리)

전체 셀렉트를 진행 기존 데이터인지 판단 병합 ⇒ 머지

→ @GeneratedValue 어노테이션을통해 PK를 관리한다면 전부 save메서드로만 사용해도 일단은 괜찮다. save메서드의 persist구문에서 null로 인식되고 DB에 삽입직전에 @GeneratedValue에 의해 값이 생긴다. 하지만 해당 어노테이션없이 특정 문자열을 PK로 사용하게 되는 경우 SDJ는 
”PK가 null값이 아니네? 이미 DB 있는거구나!!“ 라고 이해하고 merge구문으로 엔티티를 넘기게된다. 여기서

1. 병합되기 위한 자리를 찾는 select쿼리(1) → 이제서야 새로운 값인걸 판단하고 insert쿼리(2)
2. 병합되기 위한 자리를 찾는 select쿼리(1) → 자리를 찾은 후 update쿼리(2)

 merge는 항상 쿼리가 두 번 나가기 때문에 성능면에서 불리하다. 변경 감지 기능이 대부분 유리하다. (merge가 유리한 경우 조사해보기~ 뭐 무슨 엔티티가 쪼개졌다가 붙었다가 어쩌구 저쩌구)