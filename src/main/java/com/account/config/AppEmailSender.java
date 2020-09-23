package com.account.config;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AppEmailSender {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendSimpleEmail(String toEmail, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}
	
	public boolean sendHtmlMimeMessage(String toEmail, Float amount) throws MessagingException {
		System.out.println(toEmail + amount);
		
		boolean status = false;
		String text = "<div style='padding: 20px;'>Hi: <strong>"+toEmail+"</strong>, "
				+ "<br/>Thanks for Purchaging our services on AccountingApp<br/><br/>"
				+ "Amount paid: <strong>"+ amount.toString() +"</strong></div>";
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(toEmail);
			helper.setSubject("AccountingApp Payment concluseion");
			helper.setText(text, true);
			javaMailSender.send(msg);
			status = true;
			System.err.println("Email sent successfully");
		}catch(MessagingException ex) {
			ex.printStackTrace();
		}
		return status;
	}

	public boolean sendForgotPassword(String toEmail, String appURL)  {
		boolean status = false;
		String text = "<div style='padding: 20px;'>Hi: <strong>"+toEmail+"</strong>, "
				+ "<br/>Click the link below to reset your RechargeNow password<br/><br/> "
				+ "<a href='" +appURL+ "'><strong>Reset Passwort</strong></a></div>";
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(toEmail);
			helper.setSubject("AccountingApp Forgot Password");
			helper.setText(text, true);
			javaMailSender.send(msg);
			status = true;
		}catch(MessagingException ex) {
			ex.printStackTrace();
		}
		return status;
	}

}
