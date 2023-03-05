package com.sts;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.criteria.Order;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bcrp;

	@Autowired
	private UserRepository ur;

	@Autowired
	private ContactRepository cr;
	
	@Autowired
	private MyOrderRepository mor;

	// below is the common method running for all the below method
	@ModelAttribute
	public void addCommonData(Model m, Principal p) {
		String Uname = p.getName();
		System.out.printf("USERNAME", Uname);
		// get the user detail using username(email)
		User user = ur.getUserByUserName(Uname);
		System.out.printf("USER", user);
		m.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model m, Principal p) {
		// get the user details using email id
		return "Normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "Normal/add_contact_form";
	}

	// process add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,
			Principal p, HttpSession session) {
		try {
			String name = p.getName();
			User user = this.ur.getUserByUserName(name);

			// processing and uploading file...
			if (file.isEmpty()) {
				// if the file is empty then typ our message.
				System.out.println("File is empty");
				contact.setImage("Contact.png");
			} else {
				// if the file is not empty then typ our message.
				contact.setImage(file.getOriginalFilename());

				File savefile = new ClassPathResource("static/Image").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is uploaded successfully");
			}

			// below these two lines are attaching the contact to user and user to contact.
			contact.setUse(user);
			user.getContact().add(contact);
			this.ur.save(user);
			System.out.println("DATA" + contact);
			System.out.println("added to database");
			// message success
			session.setAttribute("message", new Message("Your contact is added", "success"));
		} catch (Exception e) {
			System.out.println("! " + e.getMessage());
			e.getStackTrace();
			// message error
			session.setAttribute("message", new Message("Something went wrong !! Try again...", "danger"));
		}
		return "Normal/add_contact_form";
	}

	// handler for show-contact
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model m, Principal p) {
		m.addAttribute("title", "SHOW CONTACT TABLE");
		// to send the contact list

		String Uname = p.getName();
		User user = this.ur.getUserByUserName(Uname);
		org.springframework.data.domain.Pageable pa = PageRequest.of(page, 4);
		Page<Contact> contact = this.cr.findContactByUser(user.getId(), pa);
		m.addAttribute("Contact", contact);
		m.addAttribute("currentpage", page);
		m.addAttribute("totalpage", contact.getTotalPages());

		return "Normal/show_contact";
	}

	// handler for show user private page
	@RequestMapping("/contact/{C_id}")
	public String showContactDetail(@PathVariable("C_id") Integer C_id, Model m, Principal p) {
		System.out.println("CID" + C_id);
		Optional<Contact> co = this.cr.findById(C_id);
		Contact contact = co.get();
		String Uname = p.getName();
		User user = this.ur.getUserByUserName(Uname);

		// here if the userid and contactuserid is same then the user going to see the
		// contact
		if (user.getId() == contact.getUse().getId()) {
			m.addAttribute("contact", contact);
			m.addAttribute("title", contact.getName());
		}
		return "Normal/contact_detail";
	}

	// delete-contact method for deleting the contact
	@GetMapping("/delete/{C_id}")
	public String deleteContact(@PathVariable("C_id") Integer C_id, Model m, HttpSession sess, Principal p) {
		System.out.println("CID: " + C_id);
		Contact contact = this.cr.findById(C_id).get();
		// check..the user is permited to delete the contact

		// delete old photo
		User user = this.ur.getUserByUserName(p.getName());
		user.getContact().remove(contact);
		this.ur.save(user);

		System.out.println("DELETED");
		sess.setAttribute("Message", new Message("Contact deleted successfull...", "Success"));
		return "redirect:/user/show-contact/0";
	}

	// update-contact method for deleting the contact
	@PostMapping("/update/{C_id}")
	public String updateForm(@PathVariable("C_id") Integer C_id, Model m, HttpSession sess) {
		m.addAttribute("title", "Update Contact");
		Contact contact = this.cr.findById(C_id).get();
		m.addAttribute("contact", contact);
		return "Normal/update_form";
	}

	// this method handle the update thing in this method
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,
			Model m, Principal p, HttpSession sess) {
		try {
			// oldcontact detail
			Contact oldcontact = this.cr.findById(contact.getC_id()).get();
			// image
			if (!file.isEmpty()) {
				// delete old photo
				File deletefile = new ClassPathResource("static/Image").getFile();
				File file1 = new File(deletefile, oldcontact.getImage());
				file1.delete();

				// upload new photo
				File savefile = new ClassPathResource("static/Image").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			} else {
				contact.setImage(oldcontact.getImage());
			}
			User user = this.ur.getUserByUserName(p.getName());
			contact.setUse(user);
			this.cr.save(contact);
			sess.setAttribute("message", new Message("Your contact is updated", "success"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Contact Name:" + contact.getName());
		System.out.println("Contact ID:" + contact.getC_id());
		System.out.println("Contact Number:" + contact.getPhonenumber());
		return "redirect:/user/contact/" + contact.getC_id();
	}

	// Your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("title", "Profile Page");
		return "Normal/profile";
	}

	// open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		return "Normal/settings";
	}

	// change password..handler
	public String changePassword(@RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword") String newpassword, Principal p, HttpSession sess) {
		System.out.println("oldpassword: " + oldpassword);
		System.out.println("newpassword: " + newpassword);

		String Uname = p.getName();
		User currentuser = this.ur.getUserByUserName(Uname);
		System.out.println(currentuser.getPassword());

		if (this.bcrp.matches(oldpassword, currentuser.getPassword())) {
			// Change password code
			currentuser.setPassword(this.bcrp.encode(newpassword));
			this.ur.save(currentuser);
			sess.setAttribute("message", new Message("Your password changed successfully!! ", "success"));
		} else {
			// Send email message
			sess.setAttribute("message",
					new Message("Your have enter the wrong password please try again!!", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
	
	//create order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String,Object> data,Principal p)
	{
		System.out.println("Hey order function exits.");
		//this below data variable print the amount which is wroten 
		System.out.println(data);
		int amt=Integer.parseInt(data.get("amount").toString());
		
		RazorpayClient client = new RazorpayClient("rzp_test_m8tHz3pZ8v2uj8", "3xo7tXULVImsBBMihlPZSMoh");

		JSONObject ob = new JSONObject();
		ob.put("amount","amt");
		ob.put("currency","INR");
		ob.put("receipt","txn_235425");
		//create new order
		Order order=client.Orders.create(ob);
		System.out.println(order);	
		//save the order in database
		Myorder mo=new Myorder();
		
		mo.setAmount(order.get("amount")+"");
		mo.setOrderId(order.get("orderId"));
		mo.setPaymentId(null);
		mo.setStatus("created");
		mo.setUser(this.ur.getUserByUserName(p.getName()));
		mo.setReceipt(order.get("receipt"));
		this.mor.save(mo);
		
		//if you want you can save this to your data..
		return order.toString();
	}
}
