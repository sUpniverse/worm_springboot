package net.sup2.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sup2.domain.Question;
import net.sup2.domain.QuestionRepository;
import net.sup2.domain.Result;
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
	public String show(@PathVariable Long id,Model model,HttpSession session) {
		
		model.addAttribute("question",questionRepository.findById(id).get());
		return "/qna/show";
		
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model,HttpSession session) {
		
			Question question = questionRepository.findById(id).get();
			Result result = valid(session, question);
			if(!result.isValid()) {
				model.addAttribute("errorMessage",result.getErrorMessage());
				return "/user/login";
			}
							
			model.addAttribute("question",question);
			return "/qna/updateform";
			
	}
	
	private Result valid(HttpSession session, Question question) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인이 필요합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!question.isSameUser(loginUser)) {
			return Result.fail("자신이 쓴 글만 수정,삭제가 가능합니다.");
		}
		
		return Result.OK();		
	}	
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id,String title, String contents, HttpSession session, Model model) {
		
		Question question = questionRepository.findById(id).get();
		Result result = valid(session, question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage",result.getErrorMessage());
			return "/user/login";
		}
		
		question.update(title, contents);
		questionRepository.save(question);
		return "redirect:/questions/{id}";
			
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id,HttpSession session,Model model) {
		Question question = questionRepository.findById(id).get();
		Result result = valid(session, question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage",result.getErrorMessage());
			return "/user/login";
		}
			
			questionRepository.deleteById(id);			
			return "redirect:/";		 			
	}	
}
