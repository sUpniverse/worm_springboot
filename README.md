# worm_springboot

#### 반복주기 2

- 회원가입 기능 구현

#### 반복주기 3

- H2 database 이용하기
  - locallhost:8080/h2-console 로그인하기

- Jpa 설치하기 

  - jap란?  java를 이용하여 sql의 접근할때 불편한 점들을 개선한 도구

- User dao 를 이용한 data 등록

  - User class 에 `@Entity`를 선언하여 entity임을 알려준다.

  ```Java
  @Entity
  public class User {
  	
  	@Id
  	@GeneratedValue
  	private long id;
  	
  	@Column(nullable=false, length=20)
  	private String userId;
  	private String email;
  	private String name;
  	private String password;
  }
  ```

  - `@Id`를 통해 Primary key 임을 알려준다.
  - `@GenerateValue`를 통해 Auto Increment를 설정해준다.
  - `@Column`을 통해 지정된 필드나 속성을 매핑하여준다.

- Mustach를 이용한 표현식

  ```html
  <!--  include라는 기능 루비의 yeild랑 같은 -->
  {{> /include/navigation}}
  ```

#### 반복주기 4

- 로그인 기능 구현
- jpa의 test data 넣기 (회원가입을 계속해야 하는 불편해소)
  - Resources/ 하위 파일에 import.sql 파일을 만든다.
  - .sql 파일에 query문을 작성하여 test data를 넣어논다.

