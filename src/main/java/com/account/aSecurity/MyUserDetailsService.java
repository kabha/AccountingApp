package com.account.aSecurity;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.account.common.MyUtils;
import com.account.dao.UserRepository;
import com.account.modal.AppUser;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private MyUtils myUtils;
	@Autowired
	private UserRepository repository;

	@Override
    public UserDetails loadUserByUsername(String email) {
		User loggedInUser = null;
		try {
	        AppUser user = repository.findByEmailId(email);
	        loggedInUser = new User(user.getEmailId(), myUtils.decrypt(user.getPassword()), new ArrayList<>());
		}catch(UsernameNotFoundException ex) {
			System.out.println(ex.getMessage());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return loggedInUser;
    }
}