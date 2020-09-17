package com.account.web;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.aSecurity.JwtUtils;
import com.account.common.AppConstants;
import com.account.modal.AppUser;
import com.account.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
    private AuthenticationManager authManager;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserService userService;
	private String Status = "status";
	private String Message = "message";
	private String UserDetailStr = "userDetails";

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("")
	public String getUsers() {
		logger.info("-------------getUsers---------------");
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
			jsonResponse.put("users", userService.findAllUsers());
		} catch (Exception ex) {
			jsonResponse.put(Status, 0);
			jsonResponse.put(Message, AppConstants.SOMETHING_WRONG_MSG);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}

	@PostMapping("/login")
	public String login(@RequestBody AppUser user) {
		logger.info("-------------LOGIN---------------");
		JSONObject jsonResponse = new JSONObject();
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailId(), user.getPassword()));
			final String jwtToken = jwtUtils.generateToken(user.getEmailId());
			AppUser savedUser = userService.findUserByEmailId(user.getEmailId());
			savedUser.setAccessToken(jwtToken);
			
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
			jsonResponse.put(UserDetailStr, new JSONObject(savedUser));
		}
		catch (BadCredentialsException ex) {
			jsonResponse.put(Status, 0);
			jsonResponse.put(Message, AppConstants.WRONG_EMAIL_OR_PWD);
			ex.printStackTrace();
		}
		catch (Exception ex) {
			jsonResponse.put(Status, 0);
			jsonResponse.put(Message, AppConstants.WRONG_EMAIL_OR_PWD);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@PostMapping(value = {"/save-user"}, consumes = {"application/json"})
	public String saveUser(@RequestBody AppUser user) {
		logger.info("-------------saveUser---------------");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put(Status, 0);
		jsonResponse.put(Message, AppConstants.SOMETHING_WRONG_MSG);
		try {
			userService.saveUser(user);
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@GetMapping("/find-by-email/{emailId}")
	public String findUserByEmailId(@PathVariable String emailId) {
		logger.info("-------------findUserByEmailId---------------");
		JSONObject jsonResponse = new JSONObject();
		try {
			AppUser savedUser = userService.findUserByEmailId(emailId);			
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
			jsonResponse.put(UserDetailStr, new JSONObject(savedUser));
		} catch (Exception ex) {
			jsonResponse.put(Status, 0);
			jsonResponse.put(Message, AppConstants.SOMETHING_WRONG_MSG);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@GetMapping("/find/{id}")
	public AppUser getUserById(@PathVariable("id") Long id) {
		return userService.findUserById(id);
	}

	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		System.out.println("------deleteUser-----");
		userService.deleteById(id);
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("status", 0);
		jsonResponse.put("message", "Success");
		return jsonResponse.toString();
	}

}
