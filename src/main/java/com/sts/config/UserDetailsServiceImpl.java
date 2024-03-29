package com.sts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.sts.User;
import com.sts.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=ur.getUserByUserName(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user !!");
		}
		CostomerUserDetails cud=new CostomerUserDetails(user);
		return cud;
	}
}
