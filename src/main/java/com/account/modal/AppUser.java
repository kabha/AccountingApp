package com.account.modal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.account.common.AppConstants;

@Entity
public class AppUser implements Serializable {
	
	public enum Role {ADMIN, USER, USER_MANAGER}
	
	private static final long serialVersionUID = 12345L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Size(min = 3, max = 30)
	private String userName;
	@Email
	@Column(unique = true)
	@Pattern(regexp=AppConstants.EMAIL_REGEX, message="Invalid email")
	private String emailId;
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp="\\+\\d{12}", message="Invalid phoneNumber")
	private String phoneNumber;
	private String password;
	private String address;
	private Role role = Role.USER;
	private boolean active=true;
	private LocalDateTime createdOn;
	@Transient
	private String createdOnStr;
	@Transient
	private String accessToken;
	@Transient
	private List<UserCreditCard> creditCards;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedOnStr() {
		return createdOnStr;
	}
	public void setCreatedOnStr(String createdOnStr) {
		this.createdOnStr = createdOnStr;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public List<UserCreditCard> getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(List<UserCreditCard> creditCards) {
		this.creditCards = creditCards;
	}

}
