package net.sup2.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstactEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	
	/* .equals() 메소드를 쓸때 User 객체의 가지고있는 정보가 같은지 비교하는데 
	 * 그 객체의 요소중 하나만 같으면 같다고 볼것인지, 부분 몇개만 같으면 같을지, 아니면 전체가 다맞아야할지
	 * .equals() 를 재정의 하여서 판단한다.
	 * */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AbstactEntity other = (AbstactEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Long getId() {
		return id;
	}	

	public String getFormattedCreateDate() {				
		return getFormatted(createDate, "yyyy.MM.dd HH:mm:ss");
	}
	
	public String getFormattedModifiedDate() {		
		return getFormatted(modifiedDate,"yyyy.MM.dd HH:mm:ss");
	}
	
	public String getFormatted(LocalDateTime date, String format) {
		if(date == null) {			
			return "";
		}		
		return date.format(DateTimeFormatter.ofPattern(format));
	}
	

	@Override
	public String toString() {
		return "AbstactEntity [id=" + id + ", createDate=" + createDate + ", modifiedDate=" + modifiedDate + "]";
	}
	
}
