# worm_springboot

#### 반복주기 2

- 회원가입 기능 구현

#### 반복주기 3

- H2 database 이용하기
  - locallhost:8080/h2-console 로그인하기

- Jpa 설치하기 

  - jap란?  java를 이용하여 sql의 접근할때 불편한 점들을 개선한 도구

- User dto 를 이용한 data 등록

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

- Mustache를 이용한 표현식

  ```html
  <!--  include라는 기능 루비의 yeild랑 같은 -->
  {{> /include/navigation}}
  
  <!-- If  -->
  {{#user]}
  {{/user}}
  <!-- else  -->
  {{^user}}
  {{/user}}
  ```

#### 반복주기 4

- 로그인 기능 구현

  - session 추가 쉽다. 

    ```java
    @PostMapping("/login")
    	public String login(String userId,String password, HttpSession session) {
    		//userRepository interface에 finbyUserId를 선언하여 사용.
    		User user = userRepository.findByUserId(userId);
    		if(user == null) {
    			System.out.println("Login fail not exist id");
    			return "redirect:/users/loginform";		
    		}
    		if(!password.equals(user.getPassword())) {
    			System.out.println("Login fail wrong password");
    			return "redirect:/users/loginform";
    		} 
    		//session에 인식할 수 있는 session값을 넣어준다.
    		session.setAttribute("user", user);
    		System.out.println("Login success");
    		return "redirect:/";
    	}
    ```

- jpa의 test data 넣기 (회원가입을 계속해야 하는 불편해소)

  - Resources/ 하위 파일에 import.sql 파일을 만든다.
  - .sql 파일에 query문을 작성하여 test data를 넣어논다.
  - Id를 primary key로해서 `@GenerateValue` 를해서 설정했지만 auto가 되지 않을때!
    - `@GeneratedValue(strategy=GenerationType.IDENTITY) ` 를 사용하면 된다.

- 로그아웃 기능 구현 

  - session에서 `.removeAttribue("user")를 이용해서 지워주면 끝`	
  - 간단하게 get으로 구현했지만 원래는 post로 구현한다.

- Refactoring 

  - session을 통한 User 의 확인 부분을 `sessionUtils.Class`를 이용하여 정의하여 가자뎌가 사용
    - 현재 사용자의 확인을 통해 불법적인 접근을 하는 경우의 throw 부분도 구현해보기
  - `User.getpassword`를 직접 사용하는것이 아니라 `User.matchpassword` 를 통해 간접적으로 비밀번호 확인
    - 뿐만 아니라 getId()등도 마찬가지 `.matchId()` 등과 같이 고친다.
    - 이렇게 User객체등을 생성했다면 데이터만 가져와서 담아놓는것이 아니라 객체를 활용한 방법들을 생각해본다.

- 질문하기 기능 구현

  - User를 만들때와 똑같이 Question dto를 구현하도록 한다.
    - Question class 
    - QuestionRepository interface (만든 후 Controller에 @Autowired를 선언한 repository 선언)
    - Question Controller (`@RequestMapping`등을 구현해야함.)

#### 반복주기 5

- 각 객체의 관계 매핑 (회원과 질문간의) 설정

  - 루비에서도 해봤지만 @ManyToOne , @ManyToMany등 여러가지 애노테이션을 활용하여 관계 매핑이 가능

- 날짜 형식 format 및 getter 사용

  ```java
  public String getFormattedCreateDate() {
  		if(createDate == null) {			
  			return "";
  		}		
  		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
  	}
  ```

  - `index.html` 에서 `{{formattedCreateDate}}`로 가져 올수 있음 jsp에서도 사용 가능한 기능
    - Java에서의 getter를 쓰지 않아도 가져올 수 있는 기능이므로 !!!꿀팁!!! 알아두자!
  - 