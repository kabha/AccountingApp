package com.account.service;

import java.util.List;
import com.account.modal.AppUser;

public interface UserService {

	AppUser saveUser(AppUser user);
	Boolean deleteById(Long id);
	AppUser findUserById(Long id);
	AppUser findUserByEmailId(String emailId);
	List<AppUser> findAllUsers();
}
