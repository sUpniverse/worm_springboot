package net.sup2.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty
	private long id;
	
	@Column(nullable=false, length=20, unique=true)
	@JsonProperty
	private String userId;
	
	@JsonProperty
	private String email;
	
	@JsonProperty
	private String name;
	
	@JsonIgnore
	private String password;
	
	
	public long getId() {
		return id;
	}	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", name=" + name + ", password=" + password + "]";
	}

	public void update(User user) {
		this.password = user.password;
		this.name = user.name;
		this.email = user.email;		
	}	
	
	public boolean matchPassword(String checkPassword) {
		if(checkPassword == null) {
			return false;
		}
		
		return checkPassword.equals(password);
	}
	
	public boolean matchId(Long checkId) {
		if(checkId == null) {
			return false;
		}
		
		return checkId.equals(id);
	}

	
	/* .equals() 메소드를 쓸때 User 객체의 가지고있는 정보가 같은지 비교하는데 
	 * 그 객체의 요소중 하나만 같으면 같다고 볼것인지, 부분 몇개만 같으면 같을지, 아니면 전체가 다맞아야할지
	 * .equals() 를 재정의 하여서 판단한다.
	 * */
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
