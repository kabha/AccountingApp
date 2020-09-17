package com.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.common.MyUtils;
import com.account.dao.UserRepository;
import com.account.modal.AppUser;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MyUtils myUtils;
	
	public AppUser saveUser(AppUser user) {
		user.setPassword(myUtils.encrypt(user.getPassword()));
		user.setCreatedOn(LocalDateTime.now());
		return userRepository.save(user);
	}

	public Boolean deleteById(Long id) {
		Boolean flag = Boolean.valueOf(false);
		try {
			userRepository.deleteById(id);
			flag = Boolean.valueOf(true);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}
	
	public AppUser findUserById(Long id) {
		AppUser user = null;
		Optional<AppUser> list = userRepository.findById(id);
		if (list.isPresent()) {
			user = list.get();
			user.setCreatedOnStr(myUtils.formatLocalDateTimeForUILocalDateOnly(user.getCreatedOn()));
			user.setPassword(myUtils.decrypt(user.getPassword()));
		}
		return user;
	}
	public AppUser findUserByEmailId(String emailId) {
		return userRepository.findByEmailId(emailId);
	}

	public List<AppUser> findAllUsers() {
		List<AppUser> userRecords = new ArrayList<AppUser>();
		userRepository.findAll().forEach(userRecords::add);
		return userRecords;
	}

}
