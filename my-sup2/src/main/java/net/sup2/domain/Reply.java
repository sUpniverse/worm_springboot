package net.sup2.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Reply {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_to_question"))
	@JsonProperty
	private Question question;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_writer"))
	@JsonProperty
	private User writer;
	
	@Lob
	@JsonProperty
	private String contents;
	
	private LocalDateTime createDate;
	
	public Reply() {
		// TODO Auto-generated constructor stub
	}
	
	public Reply(User writer, Question question, String contents) {
		this.writer = writer;
		this.contents = contents;		
		this.question = question;
		this.createDate = LocalDateTime.now();
	}
	
	
	public String getFormattedCreateDate() {
		if(createDate == null) {			
			return "";
		}		
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public boolean isSameWriter(User loginUser) {	
		return loginUser.equals(this.writer);
	}
	

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
		Reply other = (Reply) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reply [Id=" + id + ", question=" + question + ", writer=" + writer + ", contents=" + contents
				+ ", createDate=" + createDate + "]";
	}	
	
	public boolean isSameUser(User logindUser) {
		// TODO Auto-generated method stub
		
		return this.writer.equals(logindUser);
	}

	
	
}
