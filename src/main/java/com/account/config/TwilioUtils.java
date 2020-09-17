package com.account.config;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioUtils {

	private static final String API_KEY = "ENTER_API_KEY";
	private static final String ACCOUNT_SID = "ENTER_ACCOUNT_SID";
	private static final String ACCOUNT_AUTH_TOKEN = "ENTER_ACCOUNT_AUTH_TOKEN";
	private static final String FROM_NUMBER = "ENTER_FROM_NUMBER";
	private AuthyApiClient authyApiClient;

	/**
	 * Initialize Twilio
	 */
	public TwilioUtils() {
		Twilio.init(ACCOUNT_SID, ACCOUNT_AUTH_TOKEN);
		authyApiClient = new AuthyApiClient(API_KEY);
	}

	/**
	 * Send Message by twilio
	 */
	public Message sendMessage(String toNumber, String sms) {
		Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(FROM_NUMBER), sms).create();
		System.err.println("Message sent status: "+ message.getStatus());
		System.out.println(message.getTo());
		return message;
	}

	/**
	 * get Single Message Details
	 * @param sid Message Id
	 */
	private static void getSingleMessageDetails(String sid) {
		Message message = (Message) Message.fetcher(sid).fetch();
		System.out.println(message);
	}
	
	public static void main(String[] args) throws AuthyException {
		String phoneNumber = "+919756264795";
		String sms = "Hello there!";
		
		TwilioUtils smsSender = new TwilioUtils();
		
		smsSender.sendMessage(phoneNumber, sms);
	}
}
