package net.sup2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User extends AbstactEntity {
		
	@Column(nullable=false, length=20, unique=true)
	@JsonProperty
	private String userId;
	
	@JsonProperty
	private String email;
	
	@JsonProperty
	private String name;
	
	@JsonIgnore
	private String password;	


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [" + super.toString() + " userId=" + userId + ", email=" + email + ", name=" + name + ", password=" + password + "]";
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
		
		return checkId.equals(getId());
	}

	
	

	

}
