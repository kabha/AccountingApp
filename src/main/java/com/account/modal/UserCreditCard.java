package com.account.modal;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class UserCreditCard implements Serializable {
	
	private static final long serialVersionUID = 12345L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private Long userId;
	private String cardId;
	private String cardNumber;
	private String cardNumberFull;	//Real/Original card-number
	private Integer expiryMonth;
	private Integer expiryYear;
	private String type;
	private boolean active = true;
	private LocalDateTime createdOn;
	@Transient
	private Integer cvv2;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardNumberFull() {
		return cardNumberFull;
	}
	public void setCardNumberFull(String cardNumberFull) {
		this.cardNumberFull = cardNumberFull;
	}
	public Integer getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(Integer expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public Integer getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(Integer expiryYear) {
		this.expiryYear = expiryYear;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Integer getCvv2() {
		return cvv2;
	}
	public void setCvv2(Integer cvv2) {
		this.cvv2 = cvv2;
	}

}
