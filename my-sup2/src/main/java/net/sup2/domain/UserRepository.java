package net.sup2.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	/*User에 있는 속성중에 userId를 이용하여 찾고 싶다 라고 선언하는것 (userId이기 때문에 앞글자 U를 대문자로한다.) 
	  왜냐하면? User의 속성에 Long type id로 생성을 했기때문에 Long타입 id로 밖에 검색이 되지 않는다. */
	User findByUserId(String userId);	
}
