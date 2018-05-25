package net.sup2.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
		Question newquestion = new Question(sessioneduser, title, contents);
		questionRepository.save(newquestion);
		
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String viewQuestion(@PathVariable Long id,Model model,HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginform";
		}
		model.addAttribute("question",questionRepository.findById(id).get());
		return "/qna/show";
		
	}
}
