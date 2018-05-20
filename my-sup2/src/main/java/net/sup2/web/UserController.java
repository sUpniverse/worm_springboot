package net.sup2.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sup2.domain.User;
import net.sup2.domain.UserRepository;


@Controller
@RequestMapping("/users")				//대표 Url지정을 통해 아래의 Mapping들은 추가문으로 명시
public class UserController {
	
	List<User> users = new ArrayList<>();
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";		
	}
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user :" + user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users",userRepository.findAll());
		return "/user/list";
	}
	
	@GetMapping("/{id}/form")
	public String update_form(@PathVariable Long id, Model model) {
		model.addAttribute("user",userRepository.findById(id).get());
		return "/user/updateform";		
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updateUser) {
		User user = userRepository.findById(id).get();
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}

