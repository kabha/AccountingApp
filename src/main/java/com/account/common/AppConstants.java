package com.account.common;

public class AppConstants {
	
	public static final String APP_NAME = "AccountingApp";
	public static final String APP_PORT_NUMBER = "8080";
	
	public static final String CURRENCY = "USD";
	public static final String PAYMENT_METHOD_CREDIT_CARD = "credit_card";
	public static final String PAYMENT_STATUS_CREATED = "created";	//Txn created successfully
	public static final String PAYMENT_STATUS_APPROVED = "approved";	//Txn approved by buyer
	public static final String PAYMENT_STATUS_FAILED = "failed";		//Txn fail
	
	public static final String INVALID_INPUT = "Invalid input data";
	public static final String INVALID_PHONE_NUMBER = "Invalid PhoneNumber";
	public static final String PHONE_NUMBER_ALREADY_EXIXTS = "PhoneNumber already exists";
	public static final String INVALID_EMAIL = "Invalid Email address";
	public static final String EMAIL_NOT_EXISTS = "Email address does not exist";
	public static final String EMAIL_ALREADY_EXIXTS = "Email address already exists";
	public static final String WRONG_EMAIL_OR_PWD = "Wrong email address or password";
	
	public static final String SUCCESS = "Success";
	public static final String SOMETHING_WRONG_MSG = "Soemthing went wrong, please try again";
	
	public static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	
}
