package net.sup2.domain;


import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Question extends AbstactEntity {

	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	@JsonProperty
	private User writer;
	
	@JsonProperty
	private String title;
	
	@Lob
	@JsonProperty
	private String contents;
	
	@JsonProperty
	private Integer countOfReply;	
	
	@OneToMany(mappedBy="question")
	@OrderBy("id ASC")
	private List<Reply> replys;
	

	
	public Question() {
		// TODO Auto-generated constructor stub
	}
	
	public Question(User user, String title, String contents) {
		this.writer = user;
		this.title = title;
		this.contents = contents;		
		this.countOfReply  = 0;
	}	

	public void update(String title, String contents) {		
		this.title = title;
		this.contents = contents;
	}

	public boolean isSameUser(User logindUser) {
		// TODO Auto-generated method stub
		
		return this.writer.equals(logindUser);
	}

	public void addReply() {
		this.countOfReply += 1;		
	}
	
	public void deleteReply() {
		this.countOfReply -= 1;		
	}

	
}
