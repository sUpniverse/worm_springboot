package net.sup2.web;

import javax.servlet.http.HttpSession;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sup2.domain.Question;
import net.sup2.domain.QuestionRepository;
import net.sup2.domain.Reply;
import net.sup2.domain.User;
import net.sup2.domain.replyRepository;

@RestController
@RequestMapping("/api/questions/{id}/replys")
public class APIReplyController {
	
	@Autowired
	private replyRepository replyRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	
	@PostMapping("")
	public Reply create(@PathVariable Long id, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findById(id).get();
		Reply reply = new Reply(loginUser, question,contents);		
		
		return replyRepository.save(reply);
	}
		
}
