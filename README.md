# 생각해보기

### MVC 패턴 적용
MVC 패턴을 적용하여 프로젝트를 구성
* MVC 패턴: Model, View, Controller의 약자로, 소프트웨어를 세 가지의 역할로 구분한 디자인 패턴
  * Model: 데이터베이스와 연결되어 데이터를 가져오거나 저장하는 역할
  * View: 사용자에게 보여지는 화면
  * Controller: 사용자의 요청을 받아서 데이터를 처리하고 결과를 View에 전달하는 역할
* Spring Boot에서 MVC 패턴을 적용하기 위한 나머지 
보통 View는 프론트엔드에서 처리하므로, Spring Boot에서는 Controller와 Model만 구현하면 된다.
  * Service: 비즈니스 로직을 처리하는 역할
  * Repository: 데이터베이스와 직접적으로 연결되어 데이터를 가져오거나 저장하는 역할
  * Entity: 데이터베이스의 테이블과 매핑되는 객체
  * DTO: 데이터 전송 객체로서, Entity와 View 사이에서 데이터를 전달하는 역할
  * Request, Response: 사용자의 요청과 응답을 담당하는 객체


### 개발자 자동 할당 알고리즘
새로 생성된 티켓의 컴포너트를 파악해 어떤 분야의 이슈(티켓)인지 파악하고 많이 개발해봤으면서도 덜 바쁜 사람에게 우선 할당한다

* Ticket
* Controller
  * 생선된 티켓의 컴포넌트를 정보 서비스에 넘김 
* Service:
  * 해당 티켓의 프로젝트와 유저를 기준으로 개발자 찾기 
  * 개발자에게 할당된 티켓 수를 entry로 매핑
  * 비교& return

### Get 으로 조회시 문젲ㅁ
get 으로 조회시 Entitiy가 그대로 노출되면 Domian 단에 프록시.Lazy를 걸어뒀기 때문에
임의 값으로 인한 에러가 뜰수 있다.엔티티를 고대로 노출하지 않는 습관
api 명세가 바뀔 것을 대비해서 Get은 컨트롤러 단에서 Result<> 제네릭으로 감싸주ㅝ야함 


### Additional Links : 참고 자료
* [생성 날짜를 자동으로 찍자](https://ozofweird.tistory.com/entry/%EC%82%BD%EC%A7%88-%ED%94%BC%ED%95%98%EA%B8%B0-Spring-Boot-%EB%82%A0%EC%A7%9C-%EB%8B%A4%EB%A3%A8%EA%B8%B0?category=938335)
* [REST 튜토리얼](https://spring.io/guides/tutorials/rest/)
* [Submission 핸들링하기](https://spring.io/guides/gs/handling-form-submission/)
* [JPA 공식 문서](https://spring.io/guides/gs/accessing-data-jpa/)
* [JPA 공식 문서2](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [API명세서를 자동으로 써보자 : Swagger 버전 바뀜 참고](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security)

