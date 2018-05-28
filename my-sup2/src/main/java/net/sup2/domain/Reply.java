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

@Entity
public class Reply {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_to_question"))
	private Question question;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_writer"))
	private User writer;
	
	@Lob
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
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
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reply [Id=" + Id + ", question=" + question + ", writer=" + writer + ", contents=" + contents
				+ ", createDate=" + createDate + "]";
	}	
	
}
