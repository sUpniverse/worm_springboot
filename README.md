# worm_springboot

- spring boot 와 Jpa를 이용하여..



### 반복주기 1

- AWS에 로그인하기 

  ```shell
  ssh -i **.pem ubuntu@IP_ADDRESS
  ```

- `IP_ADDRESS:8080` 으로 접속



### 반복주기 2

- 회원가입 기능 구현 , DB없이..
  - CRUD (어렵지 않다.)



### 반복주기 3

- H2 database 이용하기

  - Pom.xml로 가서 dependency에 h2 database를 설정해준다. 그 다음 application.properties에 아래를 넣는다

  ```properties
  spring.datasource.url=jdbc:h2:~/my-sup22;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
  ```

  - 위의 properties를 완성해야 데이터 베이스가 생성된다.
  - locallhost:8080/h2-console 로그인하기

- Jpa 설치하기 

  - jap란?  java를 이용하여 sql의 접근할때 불편한 점들을 개선한 도구
    - 역시 pom.xml에 가서 jpa를 추가해준다. (mvnrepository.com에 가서 확인) 

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



### 반복주기 4

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

  - mustache를 이용한 세션 아이디를 이용하기 위해선 properties에 문구를 추가해야한다.

    ```properties
        spring.mustache.expose-session-attributes=true
    ```

- jpa의 test data 넣기 (회원가입을 계속해야 하는 불편해소)

  - Resources/ 하위 파일에 import.sql 파일을 만든다.
  - .sql 파일에 query문을 작성하여 test data를 넣어논다.
  - Id를 primary key로해서 `@GenerateValue` 를해서 설정했지만 auto가 되지 않을때!
    - `@GeneratedValue(strategy=GenerationType.IDENTITY) ` 를 사용하면 된다.

- 수정하기 기능 구현

  - User class에 update 메소드를 만들어 준다.
    - findbyId로 받아온 (세션의 유저) 에 그 유저가 새로 수정한 정보를 덮어 씌워야 하기때문에

- 로그아웃 기능 구현 

  - session에서 `.removeAttribue("user")를 이용해서 지워주면 끝`	
  - 간단하게 get으로 구현했지만 원래는 post로 구현한다.

- Refactoring (session 정보를 이용하여 사용자 검증)

  - session을 통한 User 의 확인 부분을 `sessionUtils.Class`를 이용하여 정의하여 가져다가 사용

    - session을 받아와 login된 유저인지 검증 후,  session상의 User정보를 반환하기 

      ```java
      /* sessionUtils	.class */
      public static final String SESSION_USER_KEY = "sessioneduser";
      	
      	public static boolean islogedinUser(HttpSession session) {		
      		if(session == null) {
      			return false; 
      		}		
      		return true;
      	}	
      	public static User getSessionUser(HttpSession session) {
      		if(!islogedinUser(session)) {
      			return null;
      		}		
      		return (User)session.getAttribute(SESSION_USER_KEY);		
      	}
      ```

      - session의 등록된 정보는 바뀔 수 있기때문에 상수를 선언하여서 관리한다.

    - 현재 사용자의 확인을 통해 불법적인 접근을 하는 경우의 throw 부분도 구현해보기

  - `User.getpassword`를 직접 사용하는것이 아니라 `User.matchpassword` 를 통해 간접적으로 비밀번호 확인

    ```java
    /* User.class */
    public boolean ismatchPassword(String checkedpassword) {
    		if(checkedpassword == null) {
    			return false; 
    		}
    		return checkedpassword.equals(password);
    	}
    	
    	public boolean ismatchId(Long Id) {
    		if(Id == null) {
    			return false; 
    		}
    		return Id.equals(id);
    	}
    ```

    - 이렇게 User객체등을 생성했다면 데이터만 가져와서 담아놓는것이 아니라 객체를 활용한 방법들을 생각해본다.

- 질문하기 기능 구현

  - User를 만들때와 똑같이 Question dto를 구현하도록 한다.
    - Question class 
    - QuestionRepository interface (만든 후 Controller에 @Autowired를 선언한 repository 선언)
    - Question Controller (`@RequestMapping`등을 구현해야함.)



### 반복주기 5

- 각 객체의 관계 매핑 (회원과 질문간의) 설정

  - 루비에서도 해봤지만 `@ManyToOne` , `@ManyToMany`등 여러가지 애노테이션을 활용하여 관계 매핑이 가능
    - `ManyToOne` 의 경우oreignKey로 선언한다 `@JoinColum(foreignKey=@ForeignKey(name=""))`
    - `OnetoMany` 의 경우 @OnetoMany(mappedBy="")를 통해서 여러개를 가지고 있음을 선언

- 날짜 형식 format 및 getter 사용

  - Java 8 부터는 LocalDateTime을 사용한다.

  ```java
  public String getFormattedCreateDate() {
  		if(createDate == null) {			
  			return "";
  		}		
  		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
  	}
  ```

  - 아래 return 부분은 잘 외워두면 좋을듯..
  - `index.html` 에서 `{{formattedCreateDate}}`로 가져 올수 있음 jsp에서도 사용 가능한 기능
    - Java에서의 getter를 쓰지 않아도 가져올 수 있는 기능이므로 !!!꿀팁!!! 알아두자!

- 게시물 보기 기능 구현

- 답변달기

  - Question과의 관계매핑을 이용하여 만들기 (Question은 OnetoMany, Reply는 ManytoOne)

    - 특별히 Question에서 OnetoMany를 구현할때 

      ```java
      @OneToMany(mappedBy="question")
      	@OrderBy("id ASC")
      	private List<Reply> replys;
      ```

      - mappedBy를 이용하면 replys를 question에 매핑을 알아서 해준다!

  - Exception을 이용한 회원검증 리팩토링

    ```java
    private void hasPermission(HttpSession session, Question question) {
    		if(!HttpSessionUtils.isLoginUser(session)) {
    			throw new IllegalStateException("로그인이 필요합니다.");
    		}
    		User loginUser = HttpSessionUtils.getUserFromSession(session);
    		if(!question.isSameUser(loginUser)) {
    			throw new IllegalStateException("자신이 쓴 글만 수정,삭제가 가능합니다.");
    		}		
    	}
    
    @PutMapping("/{id}")
    	public String update(@PathVariable Long id,String title, String contents, HttpSession session, Model model) {
    		try {
    			Question question = questionRepository.findById(id).get();
    			hasPermission(session, question);
    			question.update(title, contents);
    			questionRepository.save(question);
    			return "redirect:/questions/{id}";
    		} catch (IllegalStateException e) {
    			model.addAttribute("message",e.getMessage());
    			return "/user/login";
    		}		
    	}
    ```

    - 해당 메서드를 선언하여서 모든 권한에 대한 관리를 할 수 있다. (메서드를 사용하는 방법까지 같이 첨부)

  - Exception이 아닌 vaild를 확인하여 회원검증 리팩토링 (Result.class는 첨부 안함)

    ```java
    private Result valid(HttpSession session, Question question) {
    		if(!HttpSessionUtils.isLoginUser(session)) {
    			return Result.fail("로그인이 필요합니다.");
    		}
    		User loginUser = HttpSessionUtils.getUserFromSession(session);
    		if(!question.isSameUser(loginUser)) {
    			return Result.fail("자신이 쓴 글만 수정,삭제가 가능합니다.");
    		}
    		
    		return Result.OK();		
    	}	
    
    @PutMapping("/{id}")
    	public String update(@PathVariable Long id,String title, String contents, HttpSession session, Model model) {
    		
    		Question question = questionRepository.findById(id).get();
    		Result result = valid(session, question);
    		if(!result.isValid()) {
    			model.addAttribute("errorMessage",result.getErrorMessage());
    			return "/user/login";
    		}
    		
    		question.update(title, contents);
    		questionRepository.save(question);
    		return "redirect:/questions/{id}";
    			
    	}
    ```

    - 위와 아래의 확연한 차이를 볼 수 있다. 

      

### 반복주기 6

- JSON API 및 AJAX를 활용해 답변 추가 / 삭제 과정 구현

- 댓글 작성버튼을 누른 후 data를 가져오기 위해 javascript를 작성한다.

  ```javascript
  $('.reply-write input[type=submit]').click(addReply);
  
  function addReply(e) {	
  	e.preventDefault();
  	console.log("눌러져부렀다");
  	
  	var queryString = $('.reply-write').serialize();
  	console.log("query :" + queryString);
  }
  ```

- Ajax를 이용해 댓글 달기를 위한 코딩

  ```javascript
  <!-- action에 담긴 url을 가져온다 -->
  var url = $('.reply-write').attr('action');
  console.log("url : " + url);
  
  <!-- ajax로 전송 -->
  $.ajax({
  	type : 'post',
  	url : url,
  	data : queryString,
  	dataType : 'json',
  	error : onError,
  	success : onSuccess		
  });
  
  <!-- 동작 성공 후 페이지 변화없이 view단에 댓글을 만들어 주기 위한 코드 -->
  function onSuccess(data, status) {	
  	console.log(data);	
  	var replytemplate = $('#replyTemplate').html();
  	var template = replytemplate.format(data.writer.userId, data.formattedCreateDate, 										  data.contents,data.question.id,data.id);
  	$('.qna-comment-slipp-articles').prepend(template);	
  	$('textarea').val("");
  }
  ```

  - 해당 json형태의 데이터를 받기 위해 Controller에 `@RestController`를 선언해준다.
  - Json api를 사용할때 url은 앞에 /api/ 를 붙여준다. (이렇게 url 처리를 많이 한다고 한다.)

- Ajax를 이용한 댓글 삭제

  ```javascript
  $('a.link-delete-article').click(deleteReply);	
  
  function deleteReply(e){
  	e.preventDefault();
  		
  	var deleteBtn = $(this);
  	var url = deleteBtn.attr('href');
  	console.log(url);
  	
  	$.ajax({
  		type : 'delete',
  		url : url,
  		dataType : 'json',
  		error : function(xhr, status) {
  			console.log("error");					
  		},
  		success : function(data, status) {
  			console.log(data);
  			if(data.valid) {
                  <!-- 성공하면 article을 지워준다 -->
  				deleteBtn.closest("article").remove();
  			} else {
  				alert(data.errorMessage);
  			}
  		}
  	});
  }
  ```

- Contoller

  ```java
  @RestController        //json을 위한 Restcontroller 선언
  @RequestMapping("/api/questions/{questionId}/replys")
  public class APIReplyController {
  	
  	@Autowired
  	private replyRepository replyRepository;
  	
  	@Autowired
  	private QuestionRepository questionRepository;	
  	
  	@PostMapping("")
  	public Reply create(@PathVariable Long questionId, String contents, HttpSession session) {
  		/* User 검증부분 및 getUSer 생략  */
  		Reply reply = new Reply(loginUser, question,contents);		
  		question.addReply();		
  		return replyRepository.save(reply);
  	}
  	
  	@DeleteMapping("/{id}")
  	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
  		/* User 검증부분 및 getUSer 생략  */
  		replyRepository.deleteById(id);
  		Question question = questionRepository.findById(questionId).get();
  		question.deleteReply();
  		questionRepository.save(question);
  		return Result.OK();
  	}
  ```

- Swagger를 이용한 API 문서화

  - [REST] API를 그려내는 방식

  - 소스코드내에서 어노테이션을 통해 직접 API 문서화

  - Dependency 추가

    ```xml
    <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>2.2.2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>2.2.2</version>
                <scope>compile</scope>
            </dependency>
    ```

  - Application에 스웨거를 Enable 해준다.

    ```java
    @SpringBootApplication
    @EnableSwagger2
    @ComponentScan("hello")
    public class Application {
     public static void main(String[] args) {
    		SpringApplication.run(MySup2Application.class, args);
    	}
    	
    	@Bean
    	public Docket newsApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("my-sup2")
                    .apiInfo(apiInfo())
                    .select()
                    .paths(PathSelectors.ant("/api/**"))
                    .build();
        }
    	     
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("My Sup2 API")
                    .description("My Sup2 API")
                    .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
                    .contact("Niklas Heidloff")
                    .license("Apache License Version 2.0")
                    .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                    .version("2.0")
                    .build();
        }
    
    ```

- 쉘 스크립트를 활용한 배포 자동화

  ```shell
  cho "welcome My Shell"
  
  TOMCAT_HOME=~/tomcat
  
  cd ~/worm_springboot
  git pull
  
  cd my-sup2
  
  ./mvnw clean package
  
  cd $TOMCAT_HOME/bin
  ./shutdown.sh
  
  cd $TOMCAT_HOME/webapps
  rm -rf ROOT
  
  mv ~/worm_springboot/my-sup2/target/my-sup2-1.0/ $TOMCAT_HOME/webapps/ROOT/
  
  cd $TOMCAT_HOME/bin
  ./startup.sh
  
  tail -500f $TOMCAT_HOME/logs/catalina.out
  ```

  - AWS 상의 폴더내에 .sh를 만들어서 해당 쉘 스크립트를 주입한다. 
