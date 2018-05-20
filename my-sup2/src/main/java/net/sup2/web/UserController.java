package net.sup2.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import net.sup2.domain.User;
import net.sup2.domain.UserRepository;


@Controller
public class UserController {
	
	List<User> users = new ArrayList<>();
	
	@Autowired
	private UserRepository userRepository;
		
	
	@PostMapping("/create")
	public String create(User user) {
		System.out.println("user :" + user);
		userRepository.save(user);
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("users",userRepository.findAll());
		return "user/list";
	}
}

