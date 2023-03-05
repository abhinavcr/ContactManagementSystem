package com.sts;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private ContactRepository cr;
	
	//Search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal p)
	{
		System.out.println(query);
		User user=this.ur.getUserByUserName(p.getName());
		List<Contact> contact=this.cr.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contact);
	}
}
