package com.sts;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForgetController {
	Random rand = new Random(1000);

	@Autowired
	private EmailService es;

	@Autowired
	private UserRepository ur;

	@Autowired
	private BCryptPasswordEncoder bcpe;

	// email id form open handler
	@RequestMapping("/forget")
	public String openEmailform() {
		return "forget_email_form";
	}

	@PostMapping("/send-otp")
	public String sentOTP(@RequestParam("email") String email, HttpSession sess) {
		System.out.println("Your email: " + email);
		// below random method generate the 6-digit otp
		// write the random method at the top inside the class body show console treate
		// it as a class and get the random value
		int otp = rand.nextInt(999999);
		System.out.println("OTP =" + otp);

		// write a code to sending the otp to the email...
		String subject = "OTP from Contact Management System";
		String message = ""
				+ "<div style='border: 2px solid blue; padding-left:5px; padding-right:5px; padding-top:5px; padding-bottom:5px;'>"
				+ "<h1>" + "OTP =" + "<b>" + otp + "</b>" + "</h1>" + "</div>";
		String to = email;
		boolean flag = this.es.emailservice(subject, message, to);
		if (flag) {
			sess.setAttribute("myotp: ", otp);
			sess.setAttribute("email: ", email);
			return "verify_otp";
		} else {
			sess.setAttribute(message, "Check your email id!!");
			return "forget_email_form";
		}
	}

	// Otp verification code
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp, HttpSession sess) {
		int myotp = (int) sess.getAttribute("myotp");
		String email = (String) sess.getAttribute("email");
		if (myotp == otp) {
			User user = this.ur.getUserByUserName(email);

			if (user == null) {
				// send error message
				sess.setAttribute("message", "User does not exits with this email !!");
				return "forget_email_form";
			} else {
				// send change password form
			}
			return "password_change_form";
		} else {
			sess.setAttribute("message", "You have entered the wrong otp try again!!");
			return "verify_otp";
		}
	}

	// password change form
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession sess) {
		String email = (String) sess.getAttribute("email");
		User user = this.ur.getUserByUserName(email);
		user.setPassword(this.bcpe.encode(newpassword));
		this.ur.save(user);
		return "redirect:/signin?change=password changed successfully...";
	}
}
