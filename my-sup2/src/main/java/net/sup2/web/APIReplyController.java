package net.sup2.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sup2.domain.Question;
import net.sup2.domain.QuestionRepository;
import net.sup2.domain.Reply;
import net.sup2.domain.Result;
import net.sup2.domain.User;
import net.sup2.domain.replyRepository;

@RestController
@RequestMapping("/api/questions/{questionId}/replys")
public class APIReplyController {
	
	@Autowired
	private replyRepository replyRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	
	@PostMapping("")
	public Reply create(@PathVariable Long questionId, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findById(questionId).get();
		Reply reply = new Reply(loginUser, question,contents);		
		
		return replyRepository.save(reply);
	}
	
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인해야합니다.");
		}
		Reply reply = replyRepository.findById(id).get();
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		if(!reply.isSameUser(loginUser)) {
			return Result.fail("자신의 댓글만 삭제 가능합니다.");
		}
		
		replyRepository.deleteById(id);
		return Result.OK();
	}
		
}
