package com.account.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.account.common.AppConstants;
import com.account.config.AppEmailSender;
import com.account.config.TwilioUtils;
import com.account.modal.AModal;
import com.account.modal.AppUser;
import com.account.modal.UserCreditCard;
import com.account.service.PaymentService;
import com.account.service.UserService;
import com.paypal.api.payments.Payment;

@RestController
@RequestMapping({ "/api/payment" })
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private AppEmailSender emailSender;
	@Autowired
	private TwilioUtils twilioUtils;
	@Autowired
	private UserService userService;
	
	@GetMapping("/test22")
	public String test22() {
		System.out.println("----------------test22---------------");
		float amount = 1;
		try {
			emailSender.sendHtmlMimeMessage("Kabhad82@gmail.com", amount);
			System.out.println("----------------Email-Send---------------");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return "Success";
	}

	@PostMapping(value = { "/payment-by-card" }, consumes = { "application/json" })
	public String paymentByCard(@RequestBody AModal paymentModal) {
		System.out.println("----------paymentByCard------------");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("status", 0);
		try {
			UserCreditCard creditCard = paymentService.findCreditCardsByUserId(paymentModal.getUserId()).get(0);
			Payment payment = paymentService.paymentByCreditCard(creditCard.getCardId(), paymentModal.getAmount(), "Test Payment");
			if(payment != null) {
				AppUser user = userService.findUserById(paymentModal.getUserId());
				emailSender.sendHtmlMimeMessage(user.getEmailId(), paymentModal.getAmount());
				String sms = "Thanks for Purchaging our services on AccountingApp. Amount paid: "+paymentModal.getAmount();
				System.out.println(user.getPhoneNumber());
				twilioUtils.sendMessage(user.getPhoneNumber(), sms);
				
				jsonResponse.put("status", 1);
				jsonResponse.put("message", AppConstants.SUCCESS);
			}else {
				jsonResponse.put("message", "Unable to take payment from this card");
			}
		} catch (Exception ex) {
			jsonResponse.put("message", AppConstants.SOMETHING_WRONG_MSG);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@PostMapping(value = { "/add-credit-card" }, consumes = { "application/json" })
	public String addCreditCard(@RequestBody UserCreditCard creditCard) {
		System.out.println("----------addCreditCard------------");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("status", 0);
		try {
			System.out.println(creditCard);
			
			
			UserCreditCard card = paymentService.saveCreditCard(creditCard);
			if(card != null) {
				jsonResponse.put("status", 1);
				jsonResponse.put("message", AppConstants.SUCCESS);
			}else {
				jsonResponse.put("message", "Please enter valid card details");
			}
		} catch (Exception ex) {
			jsonResponse.put("message", AppConstants.SOMETHING_WRONG_MSG);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}

	@GetMapping({ "/get-credit-cards/{userId}" })
	public String getCreditCardDetails(@PathVariable Long userId, HttpServletRequest request) {
		JSONObject jsonResponse = new JSONObject();
		try {
			List<UserCreditCard> creditCards = paymentService.findCreditCardsByUserId(userId);
			jsonResponse.put("status", 1);
			jsonResponse.put("message", AppConstants.SUCCESS);
			jsonResponse.put("creditCards", creditCards);
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			jsonResponse.put("status", 0);
			jsonResponse.put("message", AppConstants.SOMETHING_WRONG_MSG);
		}
		return jsonResponse.toString();
	}

	@GetMapping({ "/delete-credit-card" })
	public String deleteCreditCardDetails(@RequestParam("cardId") Long cardId, HttpServletRequest request) {
		JSONObject jsonResponse = new JSONObject();
		try {
			paymentService.deleteCreditCard(cardId);
			
			jsonResponse.put("status", 1);
			jsonResponse.put("message", AppConstants.SUCCESS);
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			jsonResponse.put("status", 0);
			jsonResponse.put("message", AppConstants.SOMETHING_WRONG_MSG);
		}
		return jsonResponse.toString();
	}


}
