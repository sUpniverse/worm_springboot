package net.sup2.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sup2.domain.Question;
import net.sup2.domain.QuestionRepository;
import net.sup2.domain.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginform";
		}
		
		return "/qna/form";
	}
	
	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginform";
		}
		
		User sessioneduser = HttpSessionUtils.getUserFromSession(session);
	
		Question newquestion = new Question(sessioneduser.getUserId(), title, contents);
		questionRepository.save(newquestion);
		
		return "redirect:/";
	}
	
}
