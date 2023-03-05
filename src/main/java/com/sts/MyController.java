package com.sts;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
	
	@Autowired
	private BCryptPasswordEncoder bcpe;
	
	@Autowired
	private UserRepository us;
    
	@RequestMapping("/")
	public String home(Model m)
	{
		m.addAttribute("title","HOME-ContactManagementSystem");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
		m.addAttribute("title","ABOUT-ContactManagementSystem");
		return "about";
	}
 
	@RequestMapping("/login")
	public String login(Model m)
	{
		m.addAttribute("title","LOGIN-ContactManagementSystem");
		return "login";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m)
	{
		m.addAttribute("title","SIGNUP-ContactManagementSystem");
		m.addAttribute("user",new User());
		return "signup";
	}
	//if you don,t want to write requestmethod then simply use @postmapping.
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult br,@RequestParam(value="agreement",defaultValue="false") boolean agreement,
			Model m,HttpSession sess)
	{
		try {
			if(!agreement)
			{
				System.out.println("you have agreed the Terms And Conditions");
				throw new Exception("you have not agreed the Terms And Conditions");
			}
			
			if(br.hasErrors())
			{
				System.out.println("ERROR"+br.toString());
				m.addAttribute("User",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
	        user.setImage("image.png");
	        user.setPassword(passwordEncoder.encoder(user.getPassword()));
			
			System.out.println("Agreement"+agreement);
			System.out.printf("USER",user);
			User result=this.us.save(user);
			m.addAttribute("user",new User());
			sess.setAttribute("message",new Message("you are success fully done!!","alert-success"));
		}
		catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("User",user);
			sess.setAttribute("message",new Message("something went wrong!!" +e.getMessage(),"alert-danger"));
		}
		return "signup";
	}
}
