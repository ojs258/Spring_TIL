package hello.core.member;

//레파지토리의 기능인 DB저장과 DB에서 ID를 값으로 찾아오는
//save()와
// 추상메소드 두개를 가지는 인터페이스
public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
