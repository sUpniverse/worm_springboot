package net.sup2.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;


import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Reply extends AbstactEntity{
		
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
	
	
	public Reply() {
		// TODO Auto-generated constructor stub
	}
	
	public Reply(User writer, Question question, String contents) {
		this.writer = writer;
		this.contents = contents;		
		this.question = question;
		
	}
	
	public boolean isSameWriter(User loginUser) {	
		return loginUser.equals(this.writer);
	}	

	@Override
	public String toString() {
		return "Reply [" + super.toString() + ", question=" + question + ", writer=" + writer + ", contents=" + contents
				+ ", ]";
	}	
	
	public boolean isSameUser(User logindUser) {
		// TODO Auto-generated method stub
		
		return this.writer.equals(logindUser);
	}	
}
